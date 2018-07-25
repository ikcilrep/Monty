package monty;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import ast.Block;
import ast.declarations.FunctionDeclarationNode;
import lexer.LexerConfig;
import lexer.MontyToken;
import parser.MontyException;
import parser.Tokens;
import parser.parsing.Parser;

public class Importing {

	public static String[] subArray(String[] array, int begin) {
		var newArray = new String[array.length - begin];
		for (int i = 0; begin < array.length; begin++, i++) {
			newArray[i] = array[begin];
		}
		return newArray;
	}

	@SuppressWarnings("unchecked")
	private static void addAllFunctions(Block block, HashMap<String, Object> addFrom) {
		for (Object value : addFrom.values()) {
			if (value instanceof FunctionDeclarationNode)
				block.addFunction((FunctionDeclarationNode) value);
			else {
				addAllFunctions(block, (HashMap<String, Object>) value);
			}
		}
	}

	private static void addFunctionFromFile(Block block, String path) {
		var text = FileIO.readFile(new File(path).getAbsolutePath());
		var importedTokens = LexerConfig.getLexer(text).getAllTokens();
		block.concat(Parser.parse(importedTokens));
	}

	private static void addFunctionsFromDirectory(Block block, File folder) {
		for (File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				addFunctionsFromDirectory(block, fileEntry);
			} else {
				addFunctionFromFile(block, fileEntry.getAbsolutePath());
			}
		}
	}

	@SuppressWarnings("unchecked")
	private static void findAndAddFunctions(Block block, String[] splited, String partOfPath, Library toSearch) {
		var sublibraries = toSearch.getSublibraries();
		Object function = null;
		int i = 0;

		for (String toImport : splited) {
			if (!sublibraries.containsKey(toImport)) {
				new MontyException("There isn't file to import:\t" + partOfPath);
			} else if ((function = sublibraries.get(toImport)) instanceof FunctionDeclarationNode) {
				block.addFunction((FunctionDeclarationNode) function);
				break;
			} else if (i + 1 >= splited.length)
				addAllFunctions(block, sublibraries);
			else
				sublibraries = (HashMap<String, Object>) function;
			i++;
		}
	}

	public static void importFile(Block block, List<MontyToken> tokens) {
		var partOfPath = Tokens.getText(tokens.subList(1, tokens.size()));
		var path = new File(Main.path).getParent() + File.separatorChar + partOfPath.replace('.', File.separatorChar)
				+ ".mt";
		var file = new File(path);
		if (file.exists()) {
			if (file.isDirectory())
				addFunctionsFromDirectory(block, file);
			addFunctionFromFile(block, path);
		} else {
			var splited = partOfPath.split("\\.");
			if (!Parser.libraries.containsKey(splited[0]))
				new MontyException("There isn't file to import:\t" + partOfPath);

			findAndAddFunctions(block, subArray(splited, 1), partOfPath, Parser.libraries.get(splited[0]));
		}
	}

}
