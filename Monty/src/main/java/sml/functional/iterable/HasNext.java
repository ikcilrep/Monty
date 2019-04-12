package sml.functional.iterable;

import java.util.ArrayList;

import ast.declarations.FunctionDeclarationNode;
import ast.expressions.OperationNode;
import parser.DataTypes;
import sml.data.Method;

final class HasNext extends Method<Iterator> {
	private FunctionDeclarationNode hasNext;

	public HasNext(Iterator parent) {
		super(parent, "hasNext", DataTypes.BOOLEAN);
		hasNext = parent.iterator.getFunction("hasNext");
	}

	@Override
	public Object call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
		return hasNext.call(arguments, callFileName, callLine);
	}

}
