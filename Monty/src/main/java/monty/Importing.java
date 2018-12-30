/*
Copyright 2018 Szymon Perlicki

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
import java.util.LinkedList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import ast.Block;
import ast.declarations.FunctionDeclarationNode;
import ast.declarations.VariableDeclarationNode;
import lexer.Lexer;
import lexer.Token;
import parser.LogError;
import parser.Tokens;
import parser.parsing.Parser;

public class Importing {
	@SuppressWarnings("unchecked")
	private static void addAllFunctions(Block block, HashMap<String, Object> addFrom, Token token) {
		for (Object value : addFrom.values())
			if (value instanceof FunctionDeclarationNode)
				block.addFunction((FunctionDeclarationNode) value, token);
			else
				addAllFunctions(block, (HashMap<String, Object>) value, token);
	}

	private static void addFunctionFromFile(Block block, String path) {
		var file = new File(path);
		block.concat(Parser.parse(Lexer.lex(FileIO.readFile(file.getAbsolutePath()), path, 1, new LinkedList<>(), 0)));
	}

	private static void addFunctionsFromDirectory(Block block, File directory) {
		for (File fileEntry : directory.listFiles()) {
			if (fileEntry.isDirectory())
				addFunctionsFromDirectory(block, fileEntry);
			else if (fileEntry.getName().endsWith(".mt"))
				addFunctionFromFile(block, fileEntry.getAbsolutePath());
			else if (fileEntry.getName().endsWith(".mtc"))
				block.concat(IOBlocks.readCompiledBlockFromFile(fileEntry.getAbsolutePath()));
			else
				new LogError(
						"File with wrong extension: " + directory.getPath() + File.separator + fileEntry.getName());
		}
	}

	private static void addSpecifiedFunctionOrVariableFromFile(Block block, Block importedBlock, String path,
			String name) {
		var doesContainVariable = importedBlock.doesContainVariable(name);
		var doesContainFunction = importedBlock.doesContainFunction(name);
		if (doesContainVariable || doesContainFunction) {
			importedBlock.run();
			if (doesContainVariable) {
				var variable = importedBlock.getVariableByName(name);
				block.addVariable(variable, variable.getFileName(), variable.getLine());
			}
			if (doesContainFunction) {
				var function = importedBlock.getFunctionByName(name);
				block.addFunction(function, function.getFileName(), function.getLine());
			}
		} else {
			new LogError("There aren't any function or variable with this name to import:\t" + name
					+ ". Look at this file:\t" + path);
		}
	}

	private static String emptyIfNull(Path path) {
		if (path == null)
			return "";
		return path.toString();
	}

	@SuppressWarnings("unchecked")
	private static void findAndAddFunctions(Block block, String[] splited, String path, Library toSearch, Token token) {
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
				addAllFunctions(block, (HashMap<String, Object>) functionVariableOrSubLibrary, token);
			else
				sublibraries = (HashMap<String, Object>) functionVariableOrSubLibrary;
			i++;
		}
	}

	public static void importFile(Block block, List<Token> tokens) {
		var partOfPath = Tokens.getText(tokens.subList(1, tokens.size()));
		var path = Paths.get("").toAbsolutePath().toString() + File.separatorChar
				+ emptyIfNull(Paths.get(Main.path).getParent()) + File.separator
				+ partOfPath.replace('.', File.separatorChar);
		var file = new File(path + ".mt");
		var parent_file = new File(file.getParent() + ".mt");
		var compiled_file = new File(path + ".mtc");
		var parent_compiled_file = new File(compiled_file.getParent() + ".mtc");
		var directory = new File(path);
		if (directory.exists() && directory.isDirectory())
			addFunctionsFromDirectory(block, directory);
		else if (compiled_file.exists() && compiled_file.isFile())
			block.concat(IOBlocks.readCompiledBlockFromFile(compiled_file.getPath()));
		else if (file.exists() && file.isFile())
			addFunctionFromFile(block, file.getPath());
		else if (parent_compiled_file.exists() && parent_compiled_file.isFile()) {
			var name = compiled_file.getName().substring(0, compiled_file.getName().length() - 4);
			var importedBlock = IOBlocks.readCompiledBlockFromFile(parent_compiled_file.getPath());
			addSpecifiedFunctionOrVariableFromFile(block, importedBlock, parent_compiled_file.getPath(), name);
		} else if (parent_file.exists() && parent_file.isFile()) {
			var name = file.getName().substring(0, file.getName().length() - 3);
			var importedBlock = Parser.parse(Lexer.lex(FileIO.readFile(parent_file.getAbsolutePath()),
					parent_file.getName(), 1, new LinkedList<>(), 0));
			addSpecifiedFunctionOrVariableFromFile(block, importedBlock, parent_file.getPath(), name);
		} else {
			var splited = partOfPath.split("\\.");
			if (!Parser.libraries.containsKey(splited[0]))
				new LogError("There isn't file to import:\t" + path, tokens.get(1));
			findAndAddFunctions(block, subArray(splited, 1), path, Parser.libraries.get(splited[0]), tokens.get(1));
		}
	}

	public static void setLibraries(int from) {
		Parser.libraries = new HashMap<>();
		Parser.libraries.put("sml", new sml.Sml());
		for (int i = from; i < Main.argv.length; i++) {
			try {
				File pathToJar = new File(Main.argv[i]);

				@SuppressWarnings("resource")
				JarFile jarFile = new JarFile(pathToJar);
				Enumeration<JarEntry> e = jarFile.entries();

				URL[] urls = { new URL("jar:file:" + pathToJar + "!/") };
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
			} catch (IOException | ClassNotFoundException | InstantiationException | IllegalAccessException
					| IllegalArgumentException | InvocationTargetException | NoSuchMethodException
					| SecurityException e1) {
				e1.printStackTrace();

			}
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
