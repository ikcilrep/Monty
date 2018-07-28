package ast.statements;

import ast.NodeTypes;

public class JumpStatement extends StatementNode {
	private String name;

	public String getName() {
		return name;
	}


	public JumpStatement(String name) {
		this.name = name;
		super.nodeType = NodeTypes.JUMP_STATEMENT;
	}

}
