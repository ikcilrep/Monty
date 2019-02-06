package sml.data.array;

import java.util.ArrayList;

import ast.expressions.OperationNode;
import parser.DataTypes;
import sml.data.Method;
import sml.data.list.List;

final class ToList extends Method<Array>{

	public ToList(Array array) {
		super(array, "toList", DataTypes.ANY);
	}

	@Override
	public List call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
		setArguments(arguments, callFileName, callLine);
		return parent.toList();
	}
	
}
