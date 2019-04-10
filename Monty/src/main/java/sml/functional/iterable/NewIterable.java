package sml.functional.iterable;

import java.util.ArrayList;

import ast.Block;
import ast.declarations.FunctionDeclarationNode;
import ast.expressions.OperationNode;
import parser.DataTypes;
import parser.LogError;
import sml.data.checking.IsIterable;

public final class NewIterable extends FunctionDeclarationNode {

	public NewIterable() {
		super("Iterable", DataTypes.ANY);
		setBody(new Block(null));
		addParameter("iterable", DataTypes.ANY);
	}

	@Override
	public Object call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
		setArguments(arguments, callFileName, callLine);
		var iterable = getBody().getVariable("iterable").getValue();
		if (!IsIterable.isIterable(iterable, callFileName, callLine))
			new LogError("Iterable can be created only with iterable object.", callFileName, callLine);
		return new Iterable(iterable);
	}

}
