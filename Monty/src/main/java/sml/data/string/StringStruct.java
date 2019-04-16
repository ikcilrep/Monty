package sml.data.string;

import java.util.HashMap;

import ast.declarations.FunctionDeclarationNode;
import parser.LogError;

public final class StringStruct {
	private static HashMap<String, FunctionDeclarationNode> functions;
	static {
		functions = new HashMap<>();
		new CharAt();
		new EndsWith();
		new EqualsIgnoreCase();
		new Length();
		new LowerCase();
		new Replace();
		new ReplaceFirst();
		new Split();
		new StartsWith();
		new Substring();
		new UpperCase();
	}

	public static void addFunction(FunctionDeclarationNode function) {
		functions.put(function.getName(), function);
	}

	public static FunctionDeclarationNode getFunction(String name, String fileName, int line) {
		if (!functions.containsKey(name))
			new LogError("There isn't function with name:\t" + name, fileName, line);

		return functions.get(name);
	}

}
