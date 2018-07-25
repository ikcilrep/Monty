package sml.data.checking;

import java.math.BigInteger;
import java.util.ArrayList;

import ast.Block;
import ast.declarations.FunctionDeclarationNode;
import ast.declarations.VariableDeclarationNode;
import ast.expressions.OperationNode;
import parser.DataTypes;

public class IsInt extends FunctionDeclarationNode {

	public IsInt() {
		super("isInt", DataTypes.BOOLEAN);
		setBody(new Block(null));
		addParameter(new VariableDeclarationNode("toCheck", DataTypes.ANY));
	}

	@Override
	public Object call(ArrayList<OperationNode> arguments) {
		setArguments(arguments);
		var toCheck = getBody().getVariableByName("toCheck").getValue();
		return toCheck instanceof BigInteger;
	}

}
