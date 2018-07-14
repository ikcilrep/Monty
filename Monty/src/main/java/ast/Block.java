package ast;

import java.util.ArrayList;

public class Block extends Node {
	private ArrayList<Node> children = new ArrayList<>();
	private Node parent;

	public Block(Node parent) {
		this.parent = parent;
	}

	public Node getParent() {
		return parent;
	}

	public ArrayList<Node> getChildren() {
		return children;
	}

	public void addChild(Node child) {
		children.add(child);
	}
}
