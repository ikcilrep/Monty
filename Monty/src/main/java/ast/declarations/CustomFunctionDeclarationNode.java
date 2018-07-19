package ast.declarations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
		var variables = new HashMap<String, VariableDeclarationNode>();

		for (Map.Entry<String, VariableDeclarationNode> entry : body.getVariables().entrySet()) {
			String key = entry.getKey();
			VariableDeclarationNode value = ((VariableDeclarationNode) entry.getValue());
			variables.put(key, value.copy());
		}
		setArguments(arguments);
		Object result = null;
		try {
			result = body.run();
		} catch (StackOverflowError e) {
			new MontyException("Stack overflow at " + name + " function call");
		}
		var resultDataType = Identificator.getDataType(result);
		body.setVariables(variables);

		if (!resultDataType.equals(getType()))
			new MontyException("Function " + getName() + " should returns " + getType().toString().toLowerCase()
					+ ",\nbut returns " + resultDataType.toString().toLowerCase());
		return result;
	}
}
