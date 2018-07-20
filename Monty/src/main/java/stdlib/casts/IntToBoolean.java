package stdlib.casts;

import java.math.BigInteger;
import java.util.ArrayList;

import ast.Block;
import ast.declarations.FunctionDeclarationNode;
import ast.declarations.VariableDeclarationNode;
import ast.expressions.OperationNode;
import parser.DataTypes;

public class IntToBoolean extends FunctionDeclarationNode {

	public IntToBoolean() {
		super("intToBoolean", DataTypes.BOOLEAN);
		setBody(new Block(null));
		addParameter(new VariableDeclarationNode("integer", DataTypes.INTEGER));
	}

	public static Boolean intToBoolean(BigInteger integer) {
		return (Boolean) (integer.compareTo(BigInteger.valueOf(0)) > 0);
	}

	@Override
	public Object call(ArrayList<OperationNode> arguments) {
		setArguments(arguments);
		var integer = (BigInteger) getBody().getVariableByName("integer").getValue();

		return intToBoolean(integer);
	}

}
