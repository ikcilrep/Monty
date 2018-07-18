package stdlib.math;

import java.util.ArrayList;
import ast.Block;
import ast.declarations.FunctionDeclarationNode;
import ast.declarations.VariableDeclarationNode;
import ast.expressions.OperationNode;
import parser.DataTypes;

public class PowerFloat extends FunctionDeclarationNode {

	public PowerFloat() {
		super("powerFloat", DataTypes.FLOAT);
		setBody(new Block(null));
		addParameter(new VariableDeclarationNode("base", DataTypes.FLOAT));
		addParameter(new VariableDeclarationNode("exponent", DataTypes.FLOAT));

	}

	@Override
	public Object call(ArrayList<OperationNode> arguments) {
		setArguments(arguments);
		var body = getBody();
		var base = (Float) body.getVariableByName("base").getValue();
		var exponent = (Float) body.getVariableByName("exponent").getValue();
		return (Float) (float) Math.pow(base, exponent);
	}

}
