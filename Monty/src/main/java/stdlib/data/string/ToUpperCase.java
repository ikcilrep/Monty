package stdlib.data.string;

import java.util.ArrayList;

import ast.Block;
import ast.declarations.FunctionDeclarationNode;
import ast.declarations.VariableDeclarationNode;
import ast.expressions.OperationNode;
import parser.DataTypes;

public class ToUpperCase extends FunctionDeclarationNode {

	public ToUpperCase() {
		super("toUpperCase", DataTypes.STRING);
		setBody(new Block(null));
		addParameter(new VariableDeclarationNode("str", DataTypes.STRING));
	}

	@Override
	public Object call(ArrayList<OperationNode> arguments) {
		setArguments(arguments);
		var str = (String) getBody().getVariableByName("str").getValue();
		return str.toUpperCase();
	}

}
