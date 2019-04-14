package sml.data.list;

import java.math.BigInteger;
import java.util.ArrayList;

import ast.expressions.OperationNode;
import parser.DataTypes;
import sml.data.Method;

final class Find extends Method<List> {

	Find(List parent) {
		super(parent, "find", DataTypes.INTEGER);
		addParameter("value", DataTypes.ANY);
	}

	@Override
	public BigInteger call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
		setArguments(arguments, callFileName, callLine);
		return BigInteger.valueOf(parent.find(getBody().getVariableValue("value")));
	}

}
