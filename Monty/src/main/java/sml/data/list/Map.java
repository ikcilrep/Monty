package sml.data.list;

import java.util.ArrayList;

import ast.expressions.ConstantNode;
import ast.expressions.OperationNode;
import parser.DataTypes;
import parser.LogError;
import sml.data.Method;
import sml.functional.function.Function;

public class Map extends Method<List> {

	public Map(List parent) {
		super(parent, "map", DataTypes.ANY);
		addParameter("function", DataTypes.ANY);
	}

	@Override
	public Object call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
		setArguments(arguments, callFileName, callLine);
		var list = parent;
		var function = getBody().getVariable("function").getValue();
		Function castedFunction = null;
		if (!(function instanceof Function))
			new LogError(
					"Function have to be passed as parameter.\nUse function f with string, with name of function you want to pass as argument or lambda.",
					callFileName, callLine);
		castedFunction = (Function) function;
		var argument = new ArrayList<OperationNode>();
		argument.add(null);
		while (list != null && !list.head.equals(Empty.empty)) {
			argument.set(0, new OperationNode(new ConstantNode(list.head, DataTypes.getDataType(list.head)),
					castedFunction.getParent()));
			list.head = castedFunction.call(argument, callFileName, callLine);
			list = list.tail;
		}

		return parent;
	}

}
