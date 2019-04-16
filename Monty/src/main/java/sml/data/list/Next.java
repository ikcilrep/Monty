package sml.data.list;

import java.util.ArrayList;

import ast.expressions.OperationNode;
import sml.data.Method;

class Next extends Method<Iterator> {

	public Next(Iterator parent) {
		super(parent, "next");
	}

	@Override
	public Object call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
		setArguments(arguments, callFileName, callLine);
		return parent.next();
	}

}
