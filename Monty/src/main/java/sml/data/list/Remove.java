package sml.data.list;

import java.util.ArrayList;

import ast.expressions.OperationNode;
import parser.DataTypes;
import sml.data.Method;

public class Remove extends Method<List> {

	public Remove(List parent) {
		super(parent, "remove", DataTypes.ANY);
		addParameter("value", DataTypes.ANY);
	}

	@Override
	public List call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
		setArguments(arguments, callFileName, callLine);
		return parent.remove(getBody().getVariableValue("value"));
	}
	

}
