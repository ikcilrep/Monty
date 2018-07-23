package ast.statements;

import ast.NodeTypes;
import ast.expressions.VariableNode;
import parser.DataTypes;

public class ChangeToStatementNode extends StatementNode {
	private VariableNode toChangeType;
	private DataTypes dataType;

	public ChangeToStatementNode(VariableNode toChangeType, DataTypes dataType) {
		this.toChangeType = toChangeType;
		this.dataType = dataType;
		super.nodeType = NodeTypes.CHANGE_TO_STATEMENT;
	}

	public VariableNode getToChangeType() {
		return toChangeType;
	}


	public DataTypes getDataType() {
		return dataType;
	}

	
}