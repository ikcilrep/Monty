package stdlib.casts;

import java.util.ArrayList;

import ast.Block;
import ast.declarations.FunctionDeclarationNode;
import ast.declarations.VariableDeclarationNode;
import ast.expressions.OperationNode;
import parser.DataTypes;

public class FloatToString extends FunctionDeclarationNode {

	public FloatToString() {
		super("floatToString", DataTypes.STRING);
		setBody(new Block(null));
		addParameter(new VariableDeclarationNode("floating", DataTypes.FLOAT));
	}

	public static String floatToString(Float floating) {
		return floating.toString();

	}

	@Override
	public Object call(ArrayList<OperationNode> arguments) {
		setArguments(arguments);
		return floatToString(((Float) getBody().getVariableByName("floating").getValue()));
	}

}
