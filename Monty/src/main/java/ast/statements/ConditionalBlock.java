package ast.statements;

import ast.Block;
import ast.NodeTypes;
import ast.expressions.OperationNode;
import sml.casts.ToBoolean;

public class ConditionalBlock extends Block {
	OperationNode condition;

	public ConditionalBlock(OperationNode condition, Block parent, NodeTypes nodeType) {
		super(parent, nodeType);
		this.condition = condition;
	}

	public boolean runnedCondition() {
		return ToBoolean.toBoolean(condition.run(), getFileName(), getLine());

	}
}
