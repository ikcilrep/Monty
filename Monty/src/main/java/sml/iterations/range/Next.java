package sml.iterations.range;

import java.math.BigInteger;
import java.util.ArrayList;

import ast.expressions.OperationNode;
import parser.DataTypes;
import sml.data.Method;

class Next extends Method<Iterator> {

	public Next(Iterator parent) {
		super(parent, "next", DataTypes.INTEGER);
	}

	@Override
	public BigInteger call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
		setArguments(arguments, callFileName, callLine);
		BigInteger toReturn = parent.counter;
		parent.counter  = parent.counter.add(parent.step);
		return toReturn;
	}

}
