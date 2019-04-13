package sml.data.list;

import java.util.ArrayList;

import ast.expressions.OperationNode;
import parser.DataTypes;
import sml.data.Method;

public class Count extends Method<List> {

	public Count(List parent) {
		super(parent, "count", DataTypes.INTEGER);
		addParameter("value", DataTypes.ANY);
	}

	@Override
	public Object call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
		setArguments(arguments, callFileName, callLine);
		return parent.count(getBody().getVariableValue("value"));
	}

}
