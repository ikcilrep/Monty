package stdlib.casts;

import java.util.ArrayList;

import ast.Block;
import ast.declarations.FunctionDeclarationNode;
import ast.declarations.VariableDeclarationNode;
import ast.expressions.OperationNode;
import parser.DataTypes;

public class BooleanToString extends FunctionDeclarationNode {

	public BooleanToString() {
		super("booleanToString", DataTypes.STRING);
		setBody(new Block(null));
		addParameter(new VariableDeclarationNode("bool", DataTypes.BOOLEAN));
	}

	public static String booleanToString(Boolean bool) {
		return bool.toString();
	}

	@Override
	public Object call(ArrayList<OperationNode> arguments) {
		setArguments(arguments);
		return booleanToString(((Boolean) getBody().getVariableByName("bool").getValue()));
	}

}
