package sml.data.string;

import java.util.ArrayList;

import ast.Block;
import ast.declarations.FunctionDeclarationNode;
import ast.declarations.VariableDeclarationNode;
import ast.expressions.OperationNode;
import parser.DataTypes;

public class ReplaceAllInString extends FunctionDeclarationNode {

	public ReplaceAllInString() {
		super("replaceAllInString", DataTypes.STRING);
		setBody(new Block(null));
		addParameter(new VariableDeclarationNode("str", DataTypes.STRING));
		addParameter(new VariableDeclarationNode("toBeReplaced", DataTypes.STRING));
		addParameter(new VariableDeclarationNode("replacement", DataTypes.STRING));

	}

	@Override
	public Object call(ArrayList<OperationNode> arguments) {
		setArguments(arguments);
		var body = getBody();
		var str = (String) body.getVariableByName("str").getValue();
		var toBeReplaced = (String) body.getVariableByName("toBeReplaced").getValue();
		var replacement = (String) body.getVariableByName("replacement").getValue();
		return str.replaceAll(toBeReplaced, replacement);
	}

}
