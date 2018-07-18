package stdlib.casts;

import java.math.BigInteger;
import java.util.ArrayList;

import ast.Block;
import ast.declarations.FunctionDeclarationNode;
import ast.declarations.VariableDeclarationNode;
import ast.expressions.OperationNode;
import parser.DataTypes;
import parser.MontyException;

public class StringToInt extends FunctionDeclarationNode {

	public StringToInt() {
		super("stringToInt", DataTypes.INTEGER);
		setBody(new Block(null));
		addParameter(new VariableDeclarationNode("str", DataTypes.STRING));
	}

	@Override
	public Object call(ArrayList<OperationNode> arguments) {
		setArguments(arguments);
		String str = (String) getBody().getVariableByName("str").getValue();
		if (str.matches("[+-]?[0-9]+\\.[0-9]+"))
			return new BigInteger(str.split("\\.")[0]);
		else if (str.matches("[+-]?[0-9]+"))
			return new BigInteger(str);
		else
			new MontyException("Unknown number format for integer type:\t" + str);
		return null;
	}

}
