package ast.statements;

import ast.NodeTypes;
import ast.expressions.ExpressionNode;

public class ReturnStatementNode extends StatementNode {
	private ExpressionNode expression;

	public ReturnStatementNode(ExpressionNode expression) {
		this.expression = expression;
		super.nodeType = NodeTypes.RETURN_STATEMENT;
	}
	
	public ExpressionNode getExpression() {
		return expression;
	}
}
	