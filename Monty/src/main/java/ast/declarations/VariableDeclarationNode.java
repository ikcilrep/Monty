package ast.declarations;

import ast.NodeTypes;
import parser.DataTypes;

public class VariableDeclarationNode extends DeclarationNode implements Cloneable {
	private boolean isDynamic= false;

	private Object value;

	public VariableDeclarationNode(String name, DataTypes type) {
		super(name, type);
		super.nodeType = NodeTypes.VARIABLE_DECLARATION;
	}

	public VariableDeclarationNode copy() {
		try {
			return (VariableDeclarationNode) clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}

	public Object getValue() {
		return value;
	}

	public boolean isDynamic() {
		return isDynamic;
	}

	public void setDynamic(boolean isDynamic) {
		this.isDynamic = isDynamic;
	}

	public void setValue(Object value) {
		this.value = value;
	}

}
