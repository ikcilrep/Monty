package sml.data.list;

import java.util.ArrayList;

import ast.expressions.OperationNode;
import parser.DataTypes;
import sml.data.Method;

public class Multiplied extends Method<List>{
	public Multiplied(List parent) {
		super(parent, "$mul", DataTypes.ANY);
		addParameter("this", DataTypes.ANY);
		addParameter("times", DataTypes.INTEGER);
	}

	@Override
	public List call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
		setArguments(arguments, callFileName, callLine);
		return parent.multiplied(getBody().getIntVariableValue("times").intValue());
	}
}
