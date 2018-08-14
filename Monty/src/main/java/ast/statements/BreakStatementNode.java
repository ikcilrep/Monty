package ast.statements;

import ast.NodeTypes;

public class BreakStatementNode extends StatementNode{
	public BreakStatementNode() {
		this.nodeType = NodeTypes.BREAK_STATEMENT;
	}
}
