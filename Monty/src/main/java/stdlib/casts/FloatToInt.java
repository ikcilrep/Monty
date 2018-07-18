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

	@Override
	public Object call(ArrayList<OperationNode> arguments) {
		setArguments(arguments);
		return new BigInteger(((Float) getBody().getVariableByName("floating").getValue()).toString());
	}

}
