package sml.data.list;

import java.util.ArrayList;

import ast.expressions.OperationNode;
import sml.data.Method;

final class Replace extends Method<List> {

	Replace(List parent) {
		super(parent, "replace");
		addParameter("toBeReplaced");
		addParameter("replacement");
	}

	@Override
	public List call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
		setArguments(arguments, callFileName, callLine);
		var body = getBody();
		return parent.replace(body.getVariableValue("toBeReplaced"), body.getVariableValue("replacement"));
	}

}
