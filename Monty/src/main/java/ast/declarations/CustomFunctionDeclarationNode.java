package ast.declarations;

import java.util.ArrayList;

import ast.expressions.OperationNode;
import parser.DataTypes;
import parser.Identificator;
import parser.MontyException;

public class CustomFunctionDeclarationNode extends FunctionDeclarationNode {
	public CustomFunctionDeclarationNode(String name, DataTypes type) {
		super(name, type);

	}

	@Override
	public Object call(ArrayList<OperationNode> arguments) {
		setArguments(arguments);
		var result = body.run();
		var resultDataType = Identificator.getDataType(result);
		if (!resultDataType.equals(getType()))
			new MontyException("Function " + getName() + " should returns " + getType().toString().toLowerCase()
					+ ",\nbut returns " + resultDataType.toString().toLowerCase());
		return result;
	}
}
