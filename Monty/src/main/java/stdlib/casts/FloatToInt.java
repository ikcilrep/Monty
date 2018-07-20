package stdlib.casts;

import java.math.BigInteger;
import java.util.ArrayList;

import ast.Block;
import ast.declarations.FunctionDeclarationNode;
import ast.declarations.VariableDeclarationNode;
import ast.expressions.OperationNode;
import parser.DataTypes;

public class FloatToInt extends FunctionDeclarationNode {

	public FloatToInt() {
		super("floatToInt", DataTypes.INTEGER);
		setBody(new Block(null));
		addParameter(new VariableDeclarationNode("floating", DataTypes.FLOAT));
	}

	public static BigInteger floatToInt(Float floating) {
		return new BigInteger(floating.toString());

	}

	@Override
	public Object call(ArrayList<OperationNode> arguments) {
		setArguments(arguments);
		return floatToInt((Float) getBody().getVariableByName("floating").getValue());
	}

}
