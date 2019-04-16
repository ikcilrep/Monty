package sml.errors;

import java.util.ArrayList;

import ast.Block;
import ast.declarations.FunctionDeclarationNode;
import ast.expressions.OperationNode;
import sml.data.returning.Nothing;
import sml.data.returning.VoidType;

public final class LogError extends FunctionDeclarationNode {

	public LogError() {
		super("logError");
		setBody(new Block(null));
		addParameter("message");
	}

	@Override
	public VoidType call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
		setArguments(arguments, callFileName, callLine);
		new parser.LogError(getBody().getStringVariableValue("message"));
		return Nothing.nothing;
	}

}
