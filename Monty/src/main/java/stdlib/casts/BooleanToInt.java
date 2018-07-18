package stdlib.casts;

import java.math.BigInteger;
import java.util.ArrayList;

import ast.Block;
import ast.declarations.FunctionDeclarationNode;
import ast.declarations.VariableDeclarationNode;
import ast.expressions.OperationNode;
import parser.DataTypes;

public class BooleanToInt extends FunctionDeclarationNode {

	public BooleanToInt() {
		super("booleanToInt", DataTypes.INTEGER);
		setBody(new Block(null));
		addParameter(new VariableDeclarationNode("bool", DataTypes.BOOLEAN));
	}

	@Override
	public Object call(ArrayList<OperationNode> arguments) {
		setArguments(arguments);
		var toInt = ((Boolean) getBody().getVariableByName("bool").getValue());
		if (toInt == true)
			return BigInteger.valueOf(1);
		return BigInteger.valueOf(0);
	}

}
