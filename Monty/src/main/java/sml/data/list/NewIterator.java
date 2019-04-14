package sml.data.list;

import java.util.ArrayList;


import ast.expressions.OperationNode;
import parser.DataTypes;
import sml.data.Method;

final class NewIterator extends Method<List> {

	public NewIterator(List parent) {
		super(parent, "Iterator", DataTypes.ANY);
	}

	@Override
	public Iterator call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
		setArguments(arguments, callFileName, callLine);
		return new Iterator(parent);
	}

}
