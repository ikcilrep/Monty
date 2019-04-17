package sml.data;

import java.util.HashMap;

import ast.declarations.FunctionDeclarationNode;
import parser.LogError;

public class StaticStruct {
	protected HashMap<String, FunctionDeclarationNode> functions;

	public void addFunction(FunctionDeclarationNode function) {
		functions.put(function.getName(), function);
	}

	public FunctionDeclarationNode getFunction(String name, String fileName, int line) {
		if (!functions.containsKey(name))
			new LogError("There isn't function with name:\t" + name, fileName, line);

		return functions.get(name);
	}

	public void setFunctions(HashMap<String, FunctionDeclarationNode> functions) {
		this.functions = functions;
	}
}
