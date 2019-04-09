package sml.functional.function;

import java.util.ArrayList;

import ast.expressions.OperationNode;
import sml.data.Method;

class Call extends Method<Function>{

	public Call(Function parent) {
		super(parent, "call", parent.function.getType());		
	}

	@Override
	public Object call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
		return parent.function.call(arguments, callFileName, callLine);
	}
	
}
