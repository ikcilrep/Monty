package sml.data.list;

import java.util.ArrayList;

import ast.expressions.OperationNode;
import sml.data.Method;

final class Get extends Method<List> {

	Get(List parent) {
		super(parent, "get");
		addParameter("index");
	}

	@Override
	public Object call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
		setArguments(arguments, callFileName, callLine);
		var index = getBody().getIntVariableValue("index").intValue();
		parent.doesHaveElement(index, callFileName, callLine);
		return parent.get(index);
	}

}
