package stdlib.data.array;

import java.util.ArrayList;

import ast.Block;
import ast.declarations.FunctionDeclarationNode;
import ast.declarations.VariableDeclarationNode;
import ast.expressions.OperationNode;
import parser.DataTypes;

public class LengthOfArray extends FunctionDeclarationNode {

	public LengthOfArray() {
		super("lengthOfArray", DataTypes.INTEGER);
		setBody(new Block(null));
		addParameter(new VariableDeclarationNode("arr", DataTypes.ARRAY));
	}

	@Override
	public Object call(ArrayList<OperationNode> arguments) {
		setArguments(arguments);
		var arr = (Array) getBody().getVariableByName("arr").getValue();
		return arr.length();
	}

}
