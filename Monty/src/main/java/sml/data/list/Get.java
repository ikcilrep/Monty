package sml.data.list;

import java.math.BigInteger;
import java.util.ArrayList;

import ast.declarations.VariableDeclarationNode;
import ast.expressions.OperationNode;
import parser.DataTypes;
import parser.LogError;
import sml.data.Method;

class Get extends Method<List> {

	public Get(List list) {
		super(list, "get", DataTypes.ANY);
		addParameter(new VariableDeclarationNode("index", DataTypes.INTEGER));
	}

	@Override
	public Object call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
		setArguments(arguments, callFileName, callLine);
		var index = ((BigInteger) getBody().getVariable("index").getValue()).intValue();
		var length = parent.length();
		if (index >= length)
			new LogError("Index " + index + " is too big for " + length + " length", callFileName, callLine);
		return parent.get(index);
	}

}
