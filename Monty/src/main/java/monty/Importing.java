/*
Copyright 2018-2019 Szymon Perlicki

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package monty;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import ast.Block;
import ast.declarations.FunctionDeclarationNode;
import ast.declarations.VariableDeclarationNode;
import lexer.Lexer;
import lexer.OptimizedTokensArray;
import lexer.Token;
import parser.LogError;
import parser.Tokens;
import parser.parsing.Parser;

public class Importing {
	private static String mainPath = Paths.get("").toAbsolutePath().toString();
	private static Path fileAbsolutePath = Paths.get(Main.path).getParent();
	private static String mainFileLocation = mainPath + (mainPath.endsWith(File.separator) ? "" : File.separator)
			+ (fileAbsolutePath == null ? "" : fileAbsolutePath) + File.separator;

	@SuppressWarnings("unchecked")
	private static void importAllElementsFromJarSublibrary(Block block, HashMap<String, Object> addFrom, Token token) {
		for (Object value : addFrom.values())
			if (value instanceof FunctionDeclarationNode)
				block.addFunction((FunctionDeclarationNode) value, token);
			else
				importAllElementsFromJarSublibrary(block, (HashMap<String, Object>) value, token);
	}

	private static void importWholeFile(Block block, String path) {
		var file = new File(path);
		block.concat(Parser
				.parse(Lexer.lex(FileIO.readFile(file.getAbsolutePath()), path, 1, new OptimizedTokensArray(), 0)));
	}

	private static void importFilesFromDirectory(Block block, File directory) {
		for (File fileEntry : directory.listFiles()) {
			if (fileEntry.isDirectory())
				importFilesFromDirectory(block, fileEntry);
			else if (fileEntry.getName().endsWith(".mt"))
				importWholeFile(block, fileEntry.getAbsolutePath());
			else
				new LogError(
						"File with wrong extension: " + directory.getPath() + File.separator + fileEntry.getName());
		}
	}

	public static void addJarLibrary(OptimizedTokensArray tokens) {
		var partOfPath = Tokens.getText(tokens.subarray(1, tokens.length()));
		var path = mainFileLocation + partOfPath.replace('.', File.separatorChar) + ".jar";
		try {
			File jar = new File(path);
			if (jar.exists()) {
				@SuppressWarnings("resource")
				JarFile jarFile = new JarFile(jar);
				Enumeration<JarEntry> e = jarFile.entries();

				URL[] urls = { new URL("jar:file:" + jar + "!/") };
				URLClassLoader cl = URLClassLoader.newInstance(urls);

				while (e.hasMoreElements()) {
					JarEntry je = e.nextElement();
					if (je.isDirectory() || !je.getName().endsWith(".class")) {
						continue;
					}
					// -6 because of .class
					String className = je.getName().substring(0, je.getName().length() - 6);
					className = className.replace('/', '.');
					Class<?> c = cl.loadClass(className);
					Object instance;
					instance = c.getDeclaredConstructor().newInstance();

					if (instance instanceof Library) {
						Library lib = (Library) instance;
						Parser.libraries.put(lib.getName(), lib);
					}
				}
			} else {
				new LogError("There isn't library to import:\t" + path, tokens.get(1));
			}
		} catch (IOException | ClassNotFoundException | InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e1) {
			e1.printStackTrace();

		}
	}

	private static void importSpecifiedElementFromBlock(Block block, Block importedBlock, String path, String name) {
		var doesContainVariable = importedBlock.hasVariable(name);
		var doesContainFunction = importedBlock.hasFunction(name);
		if (doesContainVariable || doesContainFunction) {
			importedBlock.run();
			if (doesContainVariable) {
				var variable = importedBlock.getVariable(name);
				block.addVariable(variable, variable.getFileName(), variable.getLine());
			}
			if (doesContainFunction) {
				var function = importedBlock.getFunction(name);
				block.addFunction(function, function.getFileName(), function.getLine());
			}
		} else {
			new LogError("There aren't any function or variable with this name to import:\t" + name
					+ ". Look at this file:\t" + path);
		}
	}

	@SuppressWarnings("unchecked")
	private static void importElementFromJarLibrary(Block block, String[] splited, String path, Library toSearch,
			Token token) {
		var sublibraries = toSearch.getSublibraries();
		Object functionVariableOrSubLibrary = null;
		int i = 0;

		for (String toImport : splited) {
			if (!sublibraries.containsKey(toImport))
				new LogError("There isn't file to import:\t" + path, token);
			else if ((functionVariableOrSubLibrary = sublibraries.get(toImport)) instanceof FunctionDeclarationNode) {
				block.addFunction((FunctionDeclarationNode) functionVariableOrSubLibrary, token);
				break;
			} else if (functionVariableOrSubLibrary instanceof VariableDeclarationNode) {
				block.addVariable((VariableDeclarationNode) functionVariableOrSubLibrary, token);
				break;
			} else if (i + 1 >= splited.length)
				importAllElementsFromJarSublibrary(block, (HashMap<String, Object>) functionVariableOrSubLibrary,
						token);
			else
				sublibraries = (HashMap<String, Object>) functionVariableOrSubLibrary;
			i++;
		}
	}

	public static void importFile(Block block, OptimizedTokensArray tokensBeforeSemicolon) {
		var partOfPath = Tokens.getText(tokensBeforeSemicolon.subarray(1, tokensBeforeSemicolon.length()));
		var path = mainFileLocation + partOfPath.replace('.', File.separatorChar);
		var file = new File(path + ".mt");
		var parent_file = new File(file.getParent() + ".mt");
		var directory = new File(path);
		if (directory.exists() && directory.isDirectory())
			importFilesFromDirectory(block, directory);
		else if (file.exists() && file.isFile())
			importWholeFile(block, file.getPath());
		else if (parent_file.exists() && parent_file.isFile()) {
			importSpecifiedElementFromBlock(block,
					Parser.parse(Lexer.lex(FileIO.readFile(parent_file.getAbsolutePath()), parent_file.getName(), 1,
							new OptimizedTokensArray(), 0)),
					parent_file.getPath(), file.getName().substring(0, file.getName().length() - 3));
		} else {
			var splited = partOfPath.split("\\.");
			if (!Parser.libraries.containsKey(splited[0]))
				new LogError("There isn't file to import:\t" + path, tokensBeforeSemicolon.get(1));
			importElementFromJarLibrary(block, subArray(splited, 1), path, Parser.libraries.get(splited[0]),
					tokensBeforeSemicolon.get(1));
		}
	}

	private static String[] subArray(String[] array, int begin) {
		var newArray = new String[array.length - begin];
		for (int i = 0; begin < array.length; begin++, i++) {
			newArray[i] = array[begin];
		}
		return newArray;
	}

}
