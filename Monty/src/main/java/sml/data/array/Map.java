package sml.data.array;

import java.util.ArrayList;

import ast.expressions.ConstantNode;
import ast.expressions.OperationNode;
import parser.DataTypes;
import parser.LogError;
import sml.data.Method;
import sml.functional.function.Function;

class Map extends Method<Array> {

	public Map(Array parent) {
		super(parent, "map", DataTypes.ANY);
		addParameter("function", DataTypes.ANY);
	}

	@Override
	public Object call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
		setArguments(arguments, callFileName, callLine);
		var function = getBody().getVariable("function").getValue();
		Function castedFunction = null;
		if (!(function instanceof Function))
			new LogError(
					"Function have to be passed as parameter.\nUse function f with string, with name of function you want to pass as argument or lambda.",
					callFileName, callLine);
		castedFunction = (Function) function;
		var argument = new ArrayList<OperationNode>();
		argument.add(null);
		for (int i = 0; i < parent.array.length; i++) {
			argument.set(0, new OperationNode(new ConstantNode(parent.array[i], DataTypes.getDataType(parent.array[i])),
					castedFunction.getParent()));
			parent.array[i] = castedFunction.call(argument, callFileName, callLine);
		}
		return parent;
	}

}
