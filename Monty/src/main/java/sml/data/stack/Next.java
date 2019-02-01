package sml.data.stack;

import java.util.ArrayList;
import ast.expressions.OperationNode;
import parser.DataTypes;
import sml.data.Method;

class Next extends Method<Iterator> {
	public Next(Iterator iterator) {
		super(iterator, "next", DataTypes.ANY);
	}

	@Override
	public Object call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
		setArguments(arguments, callFileName, callLine);
		return parent.stack.array[parent.counter--];
	}

}
