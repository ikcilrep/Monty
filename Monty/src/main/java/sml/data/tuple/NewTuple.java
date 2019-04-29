package sml.data.tuple;

import java.util.ArrayList;

import ast.declarations.FunctionDeclarationNode;
import ast.expressions.OperationNode;

public class NewTuple extends FunctionDeclarationNode{

	public NewTuple() {
		super("Tuple");
	}

	@Override
	public Object call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
		return new Tuple(arguments);
	}

}
