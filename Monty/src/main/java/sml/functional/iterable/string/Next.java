package sml.functional.iterable.string;

import java.util.ArrayList;

import ast.expressions.OperationNode;
import parser.DataTypes;
import sml.data.Method;

final class Next extends Method<Iterator> {

	public Next(Iterator parent) {
		super(parent, "next", DataTypes.BOOLEAN);
	}

	@Override
	public Object call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
		setArguments(arguments, callFileName, callLine);
		var toReturn = Character.toString(parent.string[parent.counter]);
		parent.counter++;
		return toReturn;
	}

}
