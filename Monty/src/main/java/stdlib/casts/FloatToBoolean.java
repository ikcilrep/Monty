package stdlib.casts;

import java.util.ArrayList;

import ast.Block;
import ast.declarations.FunctionDeclarationNode;
import ast.declarations.VariableDeclarationNode;
import ast.expressions.OperationNode;
import parser.DataTypes;

public class FloatToBoolean extends FunctionDeclarationNode {

	public FloatToBoolean() {
		super("floatToBoolean", DataTypes.BOOLEAN);
		setBody(new Block(null));
		addParameter(new VariableDeclarationNode("floating", DataTypes.FLOAT));
	}

	public static Boolean floatToBoolean(Float floating) {
		return floating > 0;
	}
	
	@Override
	public Object call(ArrayList<OperationNode> arguments) {
		setArguments(arguments);
		return floatToBoolean((Float) getBody().getVariableByName("floating").getValue());
	}

}
