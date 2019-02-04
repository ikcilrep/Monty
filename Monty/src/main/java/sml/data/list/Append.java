package sml.data.list;

import java.util.ArrayList;

import ast.declarations.VariableDeclarationNode;
import ast.expressions.OperationNode;
import parser.DataTypes;
import sml.data.Method;

class Append extends Method<List> {

	public Append(List list) {
		super(list, "append", DataTypes.ANY);
		new VariableDeclarationNode("element", DataTypes.ANY);
	}

	@Override
	public List call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
		setArguments(arguments, callFileName, callLine);
		return parent.append(getBody().getVariable("element").getValue());
	}

}
