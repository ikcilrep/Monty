package ast.statements;

import ast.NodeTypes;

public class ContinueStatementNode extends StatementNode{
	public ContinueStatementNode() {
		this.nodeType = NodeTypes.CONTINUE_STATEMENT;
	}
}
