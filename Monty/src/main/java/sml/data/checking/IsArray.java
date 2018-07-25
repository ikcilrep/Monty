package sml.data.checking;

import java.util.ArrayList;

import ast.Block;
import ast.declarations.FunctionDeclarationNode;
import ast.declarations.VariableDeclarationNode;
import ast.expressions.OperationNode;
import parser.DataTypes;
import sml.data.array.Array;

public class IsArray extends FunctionDeclarationNode {

	public IsArray() {
		super("isArray", DataTypes.BOOLEAN);
		setBody(new Block(null));
		addParameter(new VariableDeclarationNode("toCheck", DataTypes.ANY));
	}

	@Override
	public Object call(ArrayList<OperationNode> arguments) {
		setArguments(arguments);
		var toCheck = getBody().getVariableByName("toCheck").getValue();
		return toCheck instanceof Array;
	}

}
