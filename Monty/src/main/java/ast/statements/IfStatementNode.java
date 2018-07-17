package ast.statements;

import ast.Block;
import ast.NodeTypes;
import ast.expressions.ExpressionNode;
import ast.expressions.OperationNode;

public class IfStatementNode extends Block {
	private OperationNode condition;
	private Block elseBody;
	
	public IfStatementNode(Block parent, OperationNode condition) {
		super(parent);
		this.condition = condition;
		super.nodeType = NodeTypes.IF_STATEMENT;
	}

	public Block getElseBody() {
		return elseBody;
	}
	public void setElseBody(Block elseBody) {
		this.elseBody = elseBody;
	}
	public OperationNode getCondition() {
		return condition;
	}
}
