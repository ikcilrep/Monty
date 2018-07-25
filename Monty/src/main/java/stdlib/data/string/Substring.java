package stdlib.data.string;

import java.math.BigInteger;
import java.util.ArrayList;

import ast.Block;
import ast.declarations.FunctionDeclarationNode;
import ast.declarations.VariableDeclarationNode;
import ast.expressions.OperationNode;
import parser.DataTypes;
import parser.MontyException;

public class Substring extends FunctionDeclarationNode {

	public Substring() {
		super("substring", DataTypes.STRING);
		setBody(new Block(null));
		addParameter(new VariableDeclarationNode("str", DataTypes.STRING));
		addParameter(new VariableDeclarationNode("begin", DataTypes.INTEGER));
		addParameter(new VariableDeclarationNode("end", DataTypes.INTEGER));

	}

	@Override
	public Object call(ArrayList<OperationNode> arguments) {
		setArguments(arguments);
		var body = getBody();
		var str = (String) body.getVariableByName("str").getValue();
		var begin = ((BigInteger) body.getVariableByName("begin").getValue()).intValue();
		var end = ((BigInteger) body.getVariableByName("end").getValue()).intValue();
		var length = str.length();
		if (end > length)
			new MontyException("End " + length + " is too large for length " + length);
		if (begin < 0)
			new MontyException("Begin cannot be lower than 0");

		return str.substring(begin, end);
	}

}
