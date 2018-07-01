package AST;

import Parser.DataTypes;

public class VariableDeclarationNode extends DeclarationNode {
	private int size;

	public VariableDeclarationNode(String name, DataTypes type, int size) {
		super(name, type);
		super.nodeType = NodeTypes.VARIABLE_DECLARATION;

		this.setSize(size);
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}
}
