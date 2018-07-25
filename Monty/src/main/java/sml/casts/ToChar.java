package sml.casts;

import java.math.BigInteger;
import java.util.ArrayList;

import ast.Block;
import ast.declarations.FunctionDeclarationNode;
import ast.declarations.VariableDeclarationNode;
import ast.expressions.OperationNode;
import parser.DataTypes;

public class ToChar extends FunctionDeclarationNode {

	public ToChar() {
		super("toChar", DataTypes.STRING);
		setBody(new Block(null));
		addParameter(new VariableDeclarationNode("integer", DataTypes.INTEGER));
	}

	@Override
	public Object call(ArrayList<OperationNode> arguments) {
		setArguments(arguments);
		var integer = ((BigInteger) getBody().getVariableByName("integer").getValue()).intValue();
		return String.valueOf((char) integer);
	}

}
