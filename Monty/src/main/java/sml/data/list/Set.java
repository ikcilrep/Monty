package sml.data.list;

import java.util.ArrayList;

import ast.expressions.OperationNode;
import parser.DataTypes;
import sml.data.Method;

final class Set extends Method<List>{

	Set(List parent) {
		super(parent, "set", DataTypes.ANY);
		addParameter("index", DataTypes.INTEGER);
		addParameter("value", DataTypes.ANY);
	}

	@Override
	public List call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
		setArguments(arguments, callFileName, callLine);
		var body = getBody();
		var index = body.getIntVariableValue("index").intValue();
		parent.doesHaveElement(index, callFileName, callLine);
		return parent.set(index, body.getVariableValue("value"));
	}

}
