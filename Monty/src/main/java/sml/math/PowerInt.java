package sml.math;

import java.math.BigInteger;
import java.util.ArrayList;

import ast.Block;
import ast.declarations.FunctionDeclarationNode;
import ast.declarations.VariableDeclarationNode;
import ast.expressions.OperationNode;
import parser.DataTypes;

public class PowerInt extends FunctionDeclarationNode {

	public PowerInt() {
		super("powerInt", DataTypes.INTEGER);
		setBody(new Block(null));
		addParameter(new VariableDeclarationNode("base", DataTypes.INTEGER));
		addParameter(new VariableDeclarationNode("exponent", DataTypes.INTEGER));

	}

	@Override
	public Object call(ArrayList<OperationNode> arguments) {
		setArguments(arguments);
		var body = getBody();
		var base = (BigInteger) body.getVariableByName("base").getValue();
		var exponent = (BigInteger) body.getVariableByName("exponent").getValue();
		return base.pow(exponent.intValue());
	}

}
