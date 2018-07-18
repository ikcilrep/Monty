package stdlib.math;

import java.util.ArrayList;

import ast.Block;
import ast.declarations.FunctionDeclarationNode;
import ast.declarations.VariableDeclarationNode;
import ast.expressions.OperationNode;
import parser.DataTypes;

public class SqrtFloat extends FunctionDeclarationNode {

	public SqrtFloat() {
		super("sqrtFloat", DataTypes.FLOAT);
		setBody(new Block(null));
		addParameter(new VariableDeclarationNode("n", DataTypes.FLOAT));
	}

	@Override
	public Object call(ArrayList<OperationNode> arguments) {
		setArguments(arguments);
		var body = getBody();
		var n = (Float) body.getVariableByName("n").getValue();
		return (Float) (float) Math.sqrt(n);
	}

}
