package sml.data.string;

import java.util.ArrayList;

import ast.Block;
import ast.declarations.FunctionDeclarationNode;
import ast.declarations.VariableDeclarationNode;
import ast.expressions.OperationNode;
import parser.DataTypes;

public class StartsWith extends FunctionDeclarationNode {

	public StartsWith() {
		super("startsWith", DataTypes.BOOLEAN);
		setBody(new Block(null));
		addParameter(new VariableDeclarationNode("str", DataTypes.STRING));
		addParameter(new VariableDeclarationNode("prefix", DataTypes.STRING));

	}

	@Override
	public Object call(ArrayList<OperationNode> arguments) {
		setArguments(arguments);
		var body = getBody();
		var str = (String) body.getVariableByName("str").getValue();
		var prefix = (String) body.getVariableByName("prefix").getValue();
		return str.startsWith(prefix);
	}

}
