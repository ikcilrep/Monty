package stdlib.casts;

import java.util.ArrayList;

import ast.Block;
import ast.declarations.FunctionDeclarationNode;
import ast.declarations.VariableDeclarationNode;
import ast.expressions.OperationNode;
import parser.DataTypes;

public class BooleanToFloat extends FunctionDeclarationNode {

	public BooleanToFloat() {
		super("booleanToFloat", DataTypes.FLOAT);
		setBody(new Block(null));
		addParameter(new VariableDeclarationNode("bool", DataTypes.BOOLEAN));
	}

	@Override
	public Object call(ArrayList<OperationNode> arguments) {
		setArguments(arguments);
		var toInt = ((Boolean) getBody().getVariableByName("bool").getValue());
		if (toInt == true)
			return (Float) 1f;
		return (Float) 0f;
	}

}
