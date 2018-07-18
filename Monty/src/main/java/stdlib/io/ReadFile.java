package stdlib.io;

import java.util.ArrayList;

import ast.Block;
import ast.declarations.FunctionDeclarationNode;
import ast.declarations.VariableDeclarationNode;
import ast.expressions.OperationNode;
import parser.DataTypes;

public class ReadFile extends FunctionDeclarationNode {

	public ReadFile() {
		super("readFile", DataTypes.STRING);
		setBody(new Block(null));
		addParameter(new VariableDeclarationNode("path", DataTypes.STRING));
	}

	@Override
	public Object call(ArrayList<OperationNode> arguments) {
		setArguments(arguments);
		return monty.FileIO.readFile((String) getBody().getVariableByName("path").getValue());
	}

}
