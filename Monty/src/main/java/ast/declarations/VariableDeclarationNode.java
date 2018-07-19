package ast.declarations;


import ast.NodeTypes;
import parser.DataTypes;

public class VariableDeclarationNode extends DeclarationNode implements Cloneable {
	private Object value;
	public VariableDeclarationNode(String name, DataTypes type) {
		super(name, type);
		super.nodeType = NodeTypes.VARIABLE_DECLARATION;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	public VariableDeclarationNode copy() {
		try {
			return (VariableDeclarationNode) clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}

}
