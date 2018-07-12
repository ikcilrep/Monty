package ast.expressions;

import ast.NodeTypes;

public class OperationNode extends ExpressionNode {
	private Object operand;
	private ExpressionNode left = null;
	private ExpressionNode right = null;
	
	public OperationNode(Object operand) {
		this.operand = operand;
		this.nodeType = NodeTypes.OPERATION;
	}

	public ExpressionNode getLeftOperand() {
		return left;
	}

	public ExpressionNode getRightOperand() {
		return right;
	}

	public Object getOperand() {
		return operand;
	}

	public void setLeftOperand(ExpressionNode left) {
		this.left = left;
	}

	public void setRightOperand(ExpressionNode right) {
		this.right = right;
	}

}
