package AST;

public class OperationNode extends ExpressionNode {
	private Object operand;
	private ExpressionNode op1 = null;
	private ExpressionNode op2 = null;

	public OperationNode(Object operand) {
		this.operand = operand;
	}

	public ExpressionNode getLeftOperand() {
		return op1;
	}

	public ExpressionNode getRightOperand() {
		return op2;
	}
	public Object getOperand() {
		return operand;
	}

	public void setLeftOperand(ExpressionNode op1) {
		this.op1 = op1;
	}

	public void setRightOperand(ExpressionNode op2) {
		this.op2 = op2;
	}

}
