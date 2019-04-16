package sml.functional.iterable.string;

import java.util.ArrayList;

import ast.expressions.OperationNode;
import sml.data.Method;

final class HasNext extends Method<Iterator> {

	public HasNext(Iterator parent) {
		super(parent, "hasNext");
	}

	@Override
	public Boolean call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
		setArguments(arguments, callFileName, callLine);
		return parent.counter < parent.string.length;
	}

}
