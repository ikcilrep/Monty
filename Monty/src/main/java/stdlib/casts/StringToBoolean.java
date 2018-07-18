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
		String str = (String) getBody().getVariableByName("str").getValue();
		Boolean doesstrEqualsTrue = str.equalsIgnoreCase("true");
		if (doesstrEqualsTrue || str.equalsIgnoreCase("false"))
			return doesstrEqualsTrue;
		else
			new MontyException("Unknown format for boolean type:\t" + str);
		return null;
	}

}
