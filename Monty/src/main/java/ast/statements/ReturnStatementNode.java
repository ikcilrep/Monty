package ast.statements;

import ast.NodeTypes;
import ast.expressions.OperationNode;

public class ReturnStatementNode extends StatementNode {
	private OperationNode expression;

	public ReturnStatementNode(OperationNode expression) {
		this.expression = expression;
		super.nodeType = NodeTypes.RETURN_STATEMENT;
	}
	
	public OperationNode getExpression() {
		return expression;
	}
}
	