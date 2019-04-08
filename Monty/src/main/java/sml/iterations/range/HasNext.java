package sml.iterations.range;

import java.util.ArrayList;

import ast.expressions.OperationNode;
import parser.DataTypes;
import sml.data.Method;

class HasNext extends Method<Iterator> {

	public HasNext(Iterator parent) {
		super(parent, "hasNext", DataTypes.BOOLEAN);
	}

	@Override
	public Boolean call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
		return parent.counter.compareTo(parent.max) < 0;
	}

}
