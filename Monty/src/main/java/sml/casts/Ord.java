package sml.casts;

import java.math.BigInteger;
import java.util.ArrayList;


import ast.Block;
import ast.declarations.FunctionDeclarationNode;
import ast.declarations.VariableDeclarationNode;
import ast.expressions.OperationNode;
import parser.DataTypes;
import parser.MontyException;

public class Ord extends FunctionDeclarationNode {

	public Ord() {
		super("ord", DataTypes.INTEGER);
		setBody(new Block(null));
		addParameter(new VariableDeclarationNode("chr", DataTypes.STRING));
	}

	@Override
	public Object call(ArrayList<OperationNode> arguments) {
		setArguments(arguments);
		var chr = (String) getBody().getVariableByName("chr").getValue();
		if (chr.length() != 1)
			new MontyException("Expected one character, but got " + chr.length() + ":\t" + chr);
		return BigInteger.valueOf(Character.getNumericValue(chr.charAt(0)));
	}

}
