package sml.data.array;

import java.math.BigInteger;
import java.util.ArrayList;

import ast.expressions.OperationNode;
import parser.DataTypes;
import sml.data.Method;

public class Mul extends Method<Array>{

	public Mul(Array parent) {
		super(parent, "$mul", DataTypes.ANY);
		addParameter("this", DataTypes.ANY);
		addParameter("times", DataTypes.INTEGER);
	}

	@Override
	public Object call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
		setArguments(arguments, callFileName, callLine);
		return parent.multiplication(((BigInteger)getBody().getVariable("times").getValue()).intValue());
	}

}
