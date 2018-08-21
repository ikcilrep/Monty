package ast.statements;

import ast.NodeTypes;

public class ContinueStatementNode extends StatementNode{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7650196634865355243L;

	public ContinueStatementNode() {
		this.nodeType = NodeTypes.CONTINUE_STATEMENT;
	}
}
