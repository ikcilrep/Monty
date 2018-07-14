package ast.expressions;

import ast.NodeTypes;
import parser.DataTypes;

public class ConstantNode extends ExpressionNode {
	private String value;
	private DataTypes type;

	public ConstantNode(String value, DataTypes type) {
		this.value = value;
		this.type = type;
		super.nodeType = NodeTypes.CONSTANT;
	}

	public String getValue() {
		return value;
	}

	public DataTypes getType() {
		return type;
	}
	
}
