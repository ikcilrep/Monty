package ast.statements;

import ast.Block;
import ast.NodeTypes;
import ast.expressions.ExpressionNode;
import ast.expressions.OperationNode;

public class WhileStatementNode extends StatementNode {
	private OperationNode condition;
	private Block body;

	public WhileStatementNode(OperationNode condition) {
		this.condition = condition;
		super.nodeType = NodeTypes.WHILE_STATEMENT;
	}

	public Block getBody() {
		return body;
	}

	public void setBody(Block body) {
		this.body = body;
	}

	public OperationNode getCondition() {
		return condition;
	}

}
