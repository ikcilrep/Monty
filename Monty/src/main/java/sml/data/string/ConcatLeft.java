package sml.data.string;

import java.util.ArrayList;

import ast.expressions.OperationNode;
import sml.data.Method;

public class ConcatLeft extends Method<StringStruct> {

	public ConcatLeft(StringStruct parent) {
		super(parent, "$add");
		addParameter("this");
		addParameter("other");

	}

	@Override
	public Object call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
		setArguments(arguments, callFileName, callLine);
		return parent.getString() + getBody().getVariable("other", callFileName, callLine).getValue().toString();
	}

}
