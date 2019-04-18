package sml.data.list;

import java.util.ArrayList;

import ast.declarations.FunctionDeclarationNode;
import ast.expressions.OperationNode;

public class NewList extends FunctionDeclarationNode {

	public NewList() {
		super("List");
	}

	@Override
	public Object call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
		return new List(arguments);
	}

}
