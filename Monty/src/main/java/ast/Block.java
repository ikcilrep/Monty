package ast;

import java.util.ArrayList;
import java.util.HashMap;

import ast.declarations.FunctionDeclarationNode;
import ast.declarations.VariableDeclarationNode;
import parser.MontyException;

public class Block extends Node {
	private ArrayList<Node> children = new ArrayList<>();
	private Block parent;
	HashMap<String, VariableDeclarationNode> variables = new HashMap<>();
	HashMap<String, FunctionDeclarationNode> functions = new HashMap<>();

	public Block(Block parent) {
		this.parent = parent;
	}

	public Block getParent() {
		return parent;
	}

	public ArrayList<Node> getChildren() {
		return children;
	}

	public void addChild(Node child) {
		children.add(child);
	}

	public void addVariable(VariableDeclarationNode variable) {
		String name = variable.getName();
		if (variables.containsKey(name))
			new MontyException("Variable " + name + "already exists.");
		variables.put(name, variable);
	}

	public void addFunction(FunctionDeclarationNode function) {
		String name = function.getName();
		if (variables.containsKey(name))
			new MontyException("Function " + name + " already exists.");
		functions.put(name, function);
	}

	public VariableDeclarationNode getVariableByName(String name) {
		Block block = this;
		while (true) {
			if (block.variables.containsKey(name))
				return block.variables.get(name);
			var parent = block.getParent();
			if (parent == null)
				new MontyException("There isn't variable with name:\t" + name);
			block = parent;
		}
	}

	public FunctionDeclarationNode getFunctionByName(String name) {
		Block block = this;
		while (true) {
			if (block.functions.containsKey(name))
				return block.functions.get(name);
			var parent = block.getParent();
			if (parent == null)
				new MontyException("There isn't variable with name:\t" + name);
			block = parent;
		}
	}
}
