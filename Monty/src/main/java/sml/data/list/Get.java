package sml.data.list;

import java.util.ArrayList;

import ast.expressions.OperationNode;
import parser.DataTypes;
import sml.data.Method;

public class Get extends Method<List>{

	public Get(List parent) {
		super(parent, "get", DataTypes.ANY);
		addParameter("index", DataTypes.INTEGER);
	}

	@Override
	public Object call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
		setArguments(arguments, callFileName, callLine);
		var index = getBody().getIntVariableValue("index").intValue();
		parent.doesHaveElement(index, callFileName, callLine);
		return parent.get(index);
	}

}
