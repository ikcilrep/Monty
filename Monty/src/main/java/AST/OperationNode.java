package AST;

public class OperationNode extends ExpressionNode {
	private Object operand;
	private ExpressionNode op1;
	private ExpressionNode op2;

	public OperationNode(Object operand) {
		this.operand = operand;
		op1 = op2 = null;
	}

	public OperationNode(Object op, ExpressionNode op1, ExpressionNode op2) {
		operand = op;
		this.op1 = op1;
		this.op2 = op2;
	}

	public ExpressionNode getLeftOperand() {
		return op1;
	}

	public ExpressionNode getRightOperand() {
		return op2;
	}


	public void setLeftOperand(ExpressionNode op1) {
		this.op1 = op1;
	}

	public void setRightOperand(ExpressionNode op2) {
		this.op2 = op2;
	}
	public Object getOperand() {
		return operand;
	}

	public void setOperand(Object operand) {
		this.operand = operand;
	}

}
