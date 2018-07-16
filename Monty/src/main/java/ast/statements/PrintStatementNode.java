package ast.statements;

import ast.NodeTypes;
import ast.expressions.OperationNode;
import parser.DataTypes;

public class PrintStatementNode extends StatementNode {
	private OperationNode expression;
	private DataTypes dataType = DataTypes.STRING;
	public PrintStatementNode(OperationNode expression, DataTypes dataType) {
		this.expression = expression;
		super.nodeType = NodeTypes.PRINT_STATEMENT;
		this.dataType = dataType;
	}
	
	public OperationNode getExpression() {
		return expression;
	}
	
	public void run() {
		System.out.println(expression.run(dataType));
	}
}
	