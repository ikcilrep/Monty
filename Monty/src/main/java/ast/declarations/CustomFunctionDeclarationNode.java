package ast.declarations;

import java.util.ArrayList;

import ast.expressions.OperationNode;
import parser.DataTypes;

public class CustomFunctionDeclarationNode extends FunctionDeclarationNode {
	public CustomFunctionDeclarationNode(String name, DataTypes type) {
		super(name, type);

	}

	@Override
	public Object call(ArrayList<OperationNode> arguments) {
		setArguments(arguments);
		var result = body.run();
		if (result != null)
			return ((OperationNode) result).run();
		return result;
	}
}
