package ast.statements;

import ast.NodeTypes;
import ast.expressions.ExpressionNode;

public class PrintStatementNode extends StatementNode {
	private ExpressionNode expression;

	public PrintStatementNode(ExpressionNode expression) {
		this.expression = expression;
		super.nodeType = NodeTypes.PRINT_STATEMENT;
	}
	
	public ExpressionNode getExpression() {
		return expression;
	}
}
	