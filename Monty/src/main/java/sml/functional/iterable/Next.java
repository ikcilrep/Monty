package sml.functional.iterable;

import java.util.ArrayList;

import ast.declarations.FunctionDeclarationNode;
import ast.expressions.OperationNode;
import sml.data.Method;

class Next extends Method<Iterator>{
	private FunctionDeclarationNode next;
	public Next(Iterator parent) {
		super(parent, "next", parent.iterator.getFunction("next").getType());
		next = parent.iterator.getFunction("next");
	}

	@Override
	public Object call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
		return next.call(arguments, callFileName, callLine);
	}

}
