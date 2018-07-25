package sml.data.string;

import java.math.BigInteger;
import java.util.ArrayList;

import ast.Block;
import ast.declarations.FunctionDeclarationNode;
import ast.declarations.VariableDeclarationNode;
import ast.expressions.OperationNode;
import parser.DataTypes;

public class LengthOfString extends FunctionDeclarationNode {

	public LengthOfString() {
		super("lengthOfString", DataTypes.INTEGER);
		setBody(new Block(null));
		addParameter(new VariableDeclarationNode("str", DataTypes.STRING));
	}

	@Override
	public Object call(ArrayList<OperationNode> arguments) {
		setArguments(arguments);
		var str = (String)getBody().getVariableByName("str").getValue();
		return new BigInteger(((Integer)str.length()).toString());
	}

}
