package ast.statements;

import ast.NodeTypes;

public class RunStatement extends StatementNode {
	private String name;

	public String getName() {
		return name;
	}


	public RunStatement(String name) {
		this.name = name;
		super.nodeType = NodeTypes.RUN_STATEMENT;
	}

}
