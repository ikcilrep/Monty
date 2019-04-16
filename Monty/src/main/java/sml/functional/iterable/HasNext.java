package sml.functional.iterable;

import java.util.ArrayList;

import ast.declarations.FunctionDeclarationNode;
import ast.expressions.OperationNode;
import sml.data.Method;

final class HasNext extends Method<Iterator> {
	private FunctionDeclarationNode hasNext;

	public HasNext(Iterator parent) {
		super(parent, "hasNext");
		hasNext = parent.iterator.getFunction("hasNext");
	}

	@Override
	public Boolean call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
		return (Boolean) hasNext.call(arguments, callFileName, callLine);
	}

}
