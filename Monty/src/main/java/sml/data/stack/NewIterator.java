package sml.data.stack;

import java.util.ArrayList;

import ast.expressions.OperationNode;
import parser.DataTypes;
import sml.data.Method;

class NewIterator extends Method<Stack> {

	public NewIterator(Stack stack) {
		super(stack, "Iterator", DataTypes.ANY);

	}

	@Override
	public Object call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
		return new Iterator(parent);
	}

}
