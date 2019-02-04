package sml.data.list;

import java.math.BigInteger;
import java.util.ArrayList;

import ast.expressions.OperationNode;
import parser.DataTypes;
import sml.data.Method;

class FindFirst extends Method<List> {

	public FindFirst(List list) {
		super(list, "findFirst", DataTypes.INTEGER);
		addParameter("element", DataTypes.ANY);
	}

	@Override
	public BigInteger call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
		setArguments(arguments, callFileName, callLine);
		return BigInteger.valueOf(parent.findFirst(getBody().getVariable("element").getValue()));
	}

}