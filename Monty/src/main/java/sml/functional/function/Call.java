package sml.functional.function;

import java.util.ArrayList;

import ast.expressions.OperationNode;
import sml.data.Method;

final class Call extends Method<Function> {

	public Call(Function parent) {
		super(parent, "call");
	}

	@Override
	public Object call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
		return parent.call(arguments, callFileName, callLine);
	}

}
