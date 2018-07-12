package ast.declarations;

import ast.Node;
import parser.DataTypes;

public abstract class DeclarationNode extends Node {
	protected String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public DataTypes getType() {
		return type;
	}

	public void setType(DataTypes type) {
		this.type = type;
	}

	protected DataTypes type;

	public DeclarationNode(String name, DataTypes type) {
		this.name = name;
		this.type = type;
	}
}
