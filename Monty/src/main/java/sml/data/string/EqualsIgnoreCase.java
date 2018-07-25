package sml.data.string;

import java.util.ArrayList;

import ast.Block;
import ast.declarations.FunctionDeclarationNode;
import ast.declarations.VariableDeclarationNode;
import ast.expressions.OperationNode;
import parser.DataTypes;

public class EqualsIgnoreCase extends FunctionDeclarationNode {

	public EqualsIgnoreCase() {
		super("equalsIgnoreCase", DataTypes.STRING);
		setBody(new Block(null));
		addParameter(new VariableDeclarationNode("str", DataTypes.STRING));
		addParameter(new VariableDeclarationNode("toCompare", DataTypes.STRING));

	}

	@Override
	public Object call(ArrayList<OperationNode> arguments) {
		setArguments(arguments);
		var str = (String) getBody().getVariableByName("str").getValue();
		var toCompare = (String) getBody().getVariableByName("toCompare").getValue();
		return str.equalsIgnoreCase(toCompare);
	}

}
