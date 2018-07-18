package stdlib.casts;

import java.util.ArrayList;

import ast.Block;
import ast.declarations.FunctionDeclarationNode;
import ast.declarations.VariableDeclarationNode;
import ast.expressions.OperationNode;
import parser.DataTypes;
import parser.MontyException;

public class StringToBoolean extends FunctionDeclarationNode {

	public StringToBoolean() {
		super("stringToBoolean", DataTypes.BOOLEAN);
		setBody(new Block(null));
		addParameter(new VariableDeclarationNode("str", DataTypes.STRING));
	}

	@Override
	public Object call(ArrayList<OperationNode> arguments) {
		setArguments(arguments);
		String toInt = (String) getBody().getVariableByName("str").getValue();
		Boolean doesToIntEqualsTrue = toInt.equalsIgnoreCase("true");
		if (doesToIntEqualsTrue || toInt.equalsIgnoreCase("false"))
			return doesToIntEqualsTrue;
		else
			new MontyException("Unknown format for boolean type:\t" + toInt);
		return null;
	}

}
