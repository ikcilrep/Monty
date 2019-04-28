package sml.data.list;

import java.math.BigInteger;
import java.util.ArrayList;

import ast.expressions.OperationNode;
import sml.data.Method;

final class Count extends Method<List> {

	Count(List parent) {
		super(parent, "count");
		addParameter("value");
	}

	@Override
	public BigInteger call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
		setArguments(arguments, callFileName, callLine);
		return BigInteger.valueOf(parent.count(getBody().getVariableValue("value", callFileName, callLine)));
	}

}
