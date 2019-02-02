package sml.data.list;

import java.util.ArrayList;

import ast.declarations.VariableDeclarationNode;
import ast.expressions.OperationNode;
import parser.DataTypes;
import sml.data.Method;

class Find extends Method<List> {

	public Find(List list) {
		super(list, "find", DataTypes.ANY);
		addParameter(new VariableDeclarationNode("element", DataTypes.ANY));
	}

	@Override
	public List call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
		setArguments(arguments, callFileName, callLine);
		return parent.find(getBody().getVariable("element").getValue());
	}

}
