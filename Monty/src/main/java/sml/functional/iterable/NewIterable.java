package sml.functional.iterable;

import java.util.ArrayList;

import ast.Block;
import ast.declarations.FunctionDeclarationNode;
import ast.expressions.OperationNode;
import parser.LogError;
import sml.data.checking.IsIterable;

public final class NewIterable extends FunctionDeclarationNode {

	public NewIterable() {
		super("Iterable");
		setBody(new Block(null));
		addParameter("iterable");
	}

	@Override
	public Iterable call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
		setArguments(arguments, callFileName, callLine);
		var iterable = getBody().getVariableValue("iterable");
		if (!IsIterable.isIterable(iterable, callFileName, callLine))
			new LogError("Iterable can be created only with iterable object.", callFileName, callLine);
		return new Iterable(iterable);
	}

}
