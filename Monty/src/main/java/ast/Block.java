package ast;

import java.util.ArrayList;
import java.util.HashSet;

import ast.declarations.VariableDeclarationNode;

public class Block extends Node {
	private ArrayList<Node> children = new ArrayList<>();
	private Block parent;
	HashSet<VariableDeclarationNode> variables = new HashSet<>();

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
		variables.add(variable);
	}
}
