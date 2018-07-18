package stdlib;

import java.math.BigInteger;
import java.util.ArrayList;

import ast.Block;
import ast.declarations.FunctionDeclarationNode;
import ast.declarations.VariableDeclarationNode;
import ast.expressions.OperationNode;
import parser.DataTypes;

public class IntToString extends FunctionDeclarationNode {

	public IntToString() {
		super("intToString", DataTypes.INTEGER);
		setBody(new Block(null));
		addParameter(new VariableDeclarationNode("integer", DataTypes.INTEGER));
	}

	@Override
	public Object call(ArrayList<OperationNode> arguments) {
		setArguments(arguments);
		return ((BigInteger) getBody().getVariableByName("integer").getValue()).toString();
	}

}
