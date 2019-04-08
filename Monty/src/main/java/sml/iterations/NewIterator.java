package sml.iterations;


import java.util.ArrayList;

import ast.expressions.OperationNode;
import parser.DataTypes;
import sml.data.Method;

public class NewIterator extends Method<Range> {

	public NewIterator(Range parent) {
		super(parent, "Iterator", DataTypes.ANY);
	}

	@Override
	public Object call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
		return new Iterator(parent);
	}

	
}
