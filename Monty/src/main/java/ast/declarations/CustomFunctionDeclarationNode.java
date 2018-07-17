package ast.declarations;

import java.util.ArrayList;


import ast.expressions.OperationNode;
import parser.DataTypes;
import parser.MontyException;

public class CustomFunctionDeclarationNode extends FunctionDeclarationNode {
	public CustomFunctionDeclarationNode(String name, DataTypes type) {
		super(name, type);
		
	}

	@Override
	public Object call(ArrayList<OperationNode> arguments) {
		if (arguments.size() > parameters.size())
			new MontyException("Too many arguments in " + name + " function call.");
		else if (arguments.size() < parameters.size())
			new MontyException("Too few arguments in " + name + " function call.");
		for (int i = 0; i < arguments.size(); i++) {
			var name = parameters.get(i).getName();
			var dataType = parameters.get(i).getType();
			if (!body.doesContainVariable(name))
				body.addVariable(new VariableDeclarationNode(name, dataType));
			body.getVariableByName(name).setValue(arguments.get(i).run());
		}
		var result = body.run();
		if (result != null)
			return ((OperationNode) result).run();
		return result;
	}
}
