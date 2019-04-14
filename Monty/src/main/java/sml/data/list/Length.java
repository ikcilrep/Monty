package sml.data.list;

import java.math.BigInteger;
import java.util.ArrayList;

import ast.expressions.OperationNode;
import parser.DataTypes;
import sml.data.Method;

final class Length extends Method<List> {

	Length(List parent) {
		super(parent, "length", DataTypes.INTEGER);
	}

	@Override
	public BigInteger call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
		setArguments(arguments, callFileName, callLine);
		return BigInteger.valueOf(parent.length());
	}

}
