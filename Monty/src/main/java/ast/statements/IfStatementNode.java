package ast.statements;

import ast.Block;
import ast.NodeTypes;
import ast.expressions.ExpressionNode;

public class IfStatementNode extends StatementNode {
	private ExpressionNode condition;
	private Block thenBody;
	private Block elseBody;
	
	public IfStatementNode(ExpressionNode condition) {
		this.condition = condition;
		super.nodeType = NodeTypes.IF_STATEMENT;
	}
	public Block getThenBody() {
		return thenBody;
	}
	public void setThenBody(Block thenBody) {
		this.thenBody = thenBody;
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
