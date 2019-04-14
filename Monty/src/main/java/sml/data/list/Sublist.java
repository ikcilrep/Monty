package sml.data.list;

import java.util.ArrayList;

import ast.expressions.OperationNode;
import parser.DataTypes;
import parser.LogError;
import sml.data.Method;

final class Sublist extends Method<List> {

	Sublist(List parent) {
		super(parent, "sublist", DataTypes.ANY);
		addParameter("begin", DataTypes.INTEGER);
		addParameter("end", DataTypes.INTEGER);
	}

	@Override
	public List call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
		setArguments(arguments, callFileName, callLine);
		var body = getBody();
		var begin = body.getIntVariableValue("begin").intValue();
		var end = body.getIntVariableValue("end").intValue();

		if (begin < 0)
			new LogError("Begin can't be negative.", callFileName, callLine);
		if (end > parent.length())
			new LogError("End can't be greater than length of list.", callFileName, callLine);
		if (begin > end)
			new LogError("Begin can't be greater or equals to end.", callFileName, callLine);
		return parent.sublist(begin, end);
	}

}
