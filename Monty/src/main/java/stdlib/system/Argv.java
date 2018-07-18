package stdlib.system;

import java.math.BigInteger;
import java.util.ArrayList;

import ast.Block;
import ast.declarations.FunctionDeclarationNode;
import ast.declarations.VariableDeclarationNode;
import ast.expressions.OperationNode;
import monty.Main;
import parser.DataTypes;
import parser.MontyException;

public class Argv extends FunctionDeclarationNode {

	public Argv() {
		super("argv", DataTypes.STRING);
		setBody(new Block(null));
		addParameter(new VariableDeclarationNode("index", DataTypes.INTEGER));
	}

	@Override
	public Object call(ArrayList<OperationNode> arguments) {
		setArguments(arguments);
		var index = ((BigInteger) getBody().getVariableByName("index").getValue()).intValue();
		if (index >= Main.argv.length)
			new MontyException("Index " + index + " is too large for length " + Main.argv.length);
		return Main.argv[index];
	}

}
