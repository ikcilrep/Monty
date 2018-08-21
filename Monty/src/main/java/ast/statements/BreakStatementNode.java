package ast.statements;

import ast.NodeTypes;

public class BreakStatementNode extends StatementNode{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1417558661844580557L;

	public BreakStatementNode() {
		this.nodeType = NodeTypes.BREAK_STATEMENT;
	}
}
