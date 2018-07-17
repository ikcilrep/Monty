package ast.statements;

import ast.NodeTypes;
import ast.expressions.OperationNode;

public class PrintStatementNode extends StatementNode {
	private OperationNode expression;
	public PrintStatementNode(OperationNode expression) {
		this.expression = expression;
		super.nodeType = NodeTypes.PRINT_STATEMENT;
	}
	
	public OperationNode getExpression() {
		return expression;
	}
	
	public void run() {
		System.out.println(expression.run());
	}
}
	