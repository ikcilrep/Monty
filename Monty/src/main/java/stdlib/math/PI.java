package stdlib.math;

import java.util.ArrayList;

import ast.declarations.FunctionDeclarationNode;
import ast.expressions.OperationNode;
import parser.DataTypes;

public class PI extends FunctionDeclarationNode {

	public PI() {
		super("PI", DataTypes.FLOAT);
	}

	@Override
	public Object call(ArrayList<OperationNode> arguments) {
		setArguments(arguments);
		return (Float) 3.14159265359f;
	}

}
 