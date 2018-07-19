package ast.statements;

import ast.NodeTypes;
import ast.expressions.OperationNode;

public class PrintStatementNode extends StatementNode {
	private OperationNode expression;
	private boolean isPrintln;

	public PrintStatementNode(OperationNode expression, boolean isPrintln) {
		this.expression = expression;
		this.isPrintln = isPrintln;
		super.nodeType = NodeTypes.PRINT_STATEMENT;
	}

	public OperationNode getExpression() {
		return expression;
	}

	public void run() {
		var toPrint = expression.run().toString();
		if (isPrintln)
			System.out.println(toPrint);
		else
			System.out.print(toPrint);
	}
}
