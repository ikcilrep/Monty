package sml.data.array;

import java.util.ArrayList;

import ast.expressions.OperationNode;
import parser.DataTypes;
import sml.data.Method;
import sml.data.stack.Stack;

final class ToStack extends Method<Array>{

	public ToStack(Array array) {
		super(array, "toStack", DataTypes.ANY);
	}

	@Override
	public Stack call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
		setArguments(arguments, callFileName, callLine);
		return parent.toStack();
	}
	
}
