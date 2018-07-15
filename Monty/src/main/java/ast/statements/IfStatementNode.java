package ast.statements;

import ast.Block;
import ast.NodeTypes;
import ast.expressions.ExpressionNode;

public class IfStatementNode extends Block {
	private ExpressionNode condition;
	private Block elseBody;
	
	public IfStatementNode(Block parent, ExpressionNode condition) {
		super(parent);
		this.condition = condition;
		super.nodeType = NodeTypes.IF_STATEMENT;
	}

	public Block getElseBody() {
		return elseBody;
	}
	public void setElseBody(Block elseBody) {
		this.elseBody = elseBody;
	}
	public ExpressionNode getCondition() {
		return condition;
	}
}
