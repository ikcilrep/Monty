package sml.data.list;

import java.util.ArrayList;

import ast.expressions.OperationNode;
import sml.data.Method;

final class Multiply extends Method<List> {
	public Multiply(List parent) {
		super(parent, "$a_mul");
		addParameter("this");
		addParameter("times");
	}

	@Override
	public List call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
		setArguments(arguments, callFileName, callLine);
		return parent.multiply(getBody().getIntVariableValue("times").intValue());
	}
}
