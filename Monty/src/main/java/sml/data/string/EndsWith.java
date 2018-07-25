package sml.data.string;

import java.util.ArrayList;

import ast.Block;
import ast.declarations.FunctionDeclarationNode;
import ast.declarations.VariableDeclarationNode;
import ast.expressions.OperationNode;
import parser.DataTypes;

public class EndsWith extends FunctionDeclarationNode {

	public EndsWith() {
		super("endsWith", DataTypes.BOOLEAN);
		setBody(new Block(null));
		addParameter(new VariableDeclarationNode("str", DataTypes.STRING));
		addParameter(new VariableDeclarationNode("suffix", DataTypes.STRING));

	}

	@Override
	public Object call(ArrayList<OperationNode> arguments) {
		setArguments(arguments);
		var body = getBody();
		var str = (String) body.getVariableByName("str").getValue();
		var suffix = (String) body.getVariableByName("suffix").getValue();
		return str.endsWith(suffix);
	}

}
