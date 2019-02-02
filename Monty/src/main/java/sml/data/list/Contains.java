package sml.data.list;

import java.util.ArrayList;

import ast.declarations.VariableDeclarationNode;
import ast.expressions.OperationNode;
import parser.DataTypes;
import sml.data.Method;

class Contains extends Method<List> {

	public Contains(List list) {
		super(list, "contains", DataTypes.BOOLEAN);
		addParameter(new VariableDeclarationNode("element", DataTypes.ANY));
	}

	@Override
	public Boolean call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
		setArguments(arguments, callFileName, callLine);
		return parent.contains(getBody().getVariableByName("element").getValue());
	}

}
