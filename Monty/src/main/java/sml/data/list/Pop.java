package sml.data.list;

import java.util.ArrayList;

import ast.expressions.OperationNode;
import parser.DataTypes;
import sml.data.Method;

final class Pop extends Method<List> {

	Pop(List parent) {
		super(parent, "pop", DataTypes.ANY);
		addParameter("index", DataTypes.INTEGER);
	}

	@Override
	public Object call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
		setArguments(arguments, callFileName, callLine);
		var index = getBody().getIntVariableValue("index").intValue();
		parent.doesHaveElement(index, callFileName, callLine);
		return parent.pop(index);
	}

}
