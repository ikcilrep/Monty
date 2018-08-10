package ast.statements;

import ast.NodeTypes;
import ast.expressions.OperationNode;

public class ThreadStatement extends StatementNode {
	private OperationNode expression;

	public OperationNode getExpression() {
		return expression;
	}

	public ThreadStatement(OperationNode expression) {
		this.expression = expression;
		super.nodeType = NodeTypes.THREAD_STATEMENT;
	}

}
