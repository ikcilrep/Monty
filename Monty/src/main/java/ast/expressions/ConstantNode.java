package ast.expressions;

import ast.NodeTypes;
import parser.DataTypes;

public class ConstantNode extends ExpressionNode {
	private Object value;
	private DataTypes type;

	public ConstantNode(Object value, DataTypes type) {
		this.value = value;
		this.type = type;
		super.nodeType = NodeTypes.CONSTANT;
	}


	public Object getValue() {
		return value;
	}

	public DataTypes getType() {
		return type;
	}
	
}
