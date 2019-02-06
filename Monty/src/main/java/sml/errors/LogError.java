package sml.errors;

import java.util.ArrayList;

import ast.Block;
import ast.declarations.FunctionDeclarationNode;
import ast.expressions.OperationNode;
import parser.DataTypes;

public class LogError extends FunctionDeclarationNode{

	public LogError() {
		super("logError", DataTypes.VOID);
		setBody(new Block(null));
		addParameter("message", DataTypes.STRING);
	}

	@Override
	public Object call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
		setArguments(arguments, callFileName, callLine);
		new parser.LogError(getBody().getVariable("message").getValue().toString());
		return null;
	}

}
