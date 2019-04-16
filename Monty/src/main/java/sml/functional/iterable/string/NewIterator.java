package sml.functional.iterable.string;

import java.util.ArrayList;

import ast.expressions.OperationNode;
import sml.data.Method;

final class NewIterator extends Method<IterableString> {

	public NewIterator(IterableString parent) {
		super(parent, "Iterator");
	}

	@Override
	public Iterator call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
		setArguments(arguments, callFileName, callLine);
		return new Iterator(parent.string);
	}

}
