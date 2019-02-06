package sml.data.array;

import java.util.ArrayList;

import ast.expressions.OperationNode;
import parser.DataTypes;
import sml.data.Method;

final class Equals extends Method<Array> {

	public Equals(Array parent) {
		super(parent, "equals", DataTypes.BOOLEAN);
		addParameter("other", DataTypes.ANY);
	}

	@Override
	public Object call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
		setArguments(arguments, callFileName, callLine);
		return parent.equals(getBody().getVariable("other").getValue());
	}

}
