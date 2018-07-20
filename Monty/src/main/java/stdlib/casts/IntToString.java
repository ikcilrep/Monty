package stdlib.casts;

import java.math.BigInteger;
import java.util.ArrayList;

import ast.Block;
import ast.declarations.FunctionDeclarationNode;
import ast.declarations.VariableDeclarationNode;
import ast.expressions.OperationNode;
import parser.DataTypes;

public class IntToString extends FunctionDeclarationNode {

	public IntToString() {
		super("intToString", DataTypes.STRING);
		setBody(new Block(null));
		addParameter(new VariableDeclarationNode("integer", DataTypes.INTEGER));
	}

	public static String intToString(BigInteger integer) {
		return integer.toString();
	}

	@Override
	public Object call(ArrayList<OperationNode> arguments) {
		setArguments(arguments);
		return intToString(((BigInteger) getBody().getVariableByName("integer").getValue()));
	}

}
