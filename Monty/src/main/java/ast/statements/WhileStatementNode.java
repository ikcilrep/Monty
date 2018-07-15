package ast.statements;

import ast.Block;
import ast.NodeTypes;
import ast.expressions.ExpressionNode;

public class WhileStatementNode extends StatementNode {
	private ExpressionNode condition;
	private Block body;

	public WhileStatementNode(ExpressionNode condition) {
		this.condition = condition;
		super.nodeType = NodeTypes.WHILE_STATEMENT;
	}

	public Block getBody() {
		return body;
	}

	public void setBody(Block body) {
		this.body = body;
	}

	public ExpressionNode getCondition() {
		return condition;
	}

}
