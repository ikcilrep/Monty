package sml.data.list;

import java.util.ArrayList;

import ast.expressions.OperationNode;
import sml.data.Method;

final class Multiplied extends Method<List> {
	public Multiplied(List parent) {
		super(parent, "$mul");
		addParameter("this");
		addParameter("times");
	}

	@Override
	public List call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
		setArguments(arguments, callFileName, callLine);
		return parent.multiplied(getBody().getIntVariableValue("times").intValue());
	}
}
