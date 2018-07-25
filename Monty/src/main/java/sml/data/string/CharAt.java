package sml.data.string;

import java.math.BigInteger;
import java.util.ArrayList;

import ast.Block;
import ast.declarations.FunctionDeclarationNode;
import ast.declarations.VariableDeclarationNode;
import ast.expressions.OperationNode;
import parser.DataTypes;
import parser.MontyException;

public class CharAt extends FunctionDeclarationNode {

	public CharAt() {
		super("charAt", DataTypes.STRING);
		setBody(new Block(null));
		addParameter(new VariableDeclarationNode("str", DataTypes.STRING));
		addParameter(new VariableDeclarationNode("index", DataTypes.STRING));
	}

	@Override
	public Object call(ArrayList<OperationNode> arguments) {
		setArguments(arguments);
		var body = getBody();
		var str = (String) body.getVariableByName("str").getValue();
		var index = ((BigInteger) body.getVariableByName("index").getValue()).intValue();
		var length= str.length();
		if (index >= length)
			new MontyException("Index " + index + " is too large for length " + length);
		return str.charAt(index);
	}

}
