package sml.data.list;

import java.util.ArrayList;

import ast.declarations.VariableDeclarationNode;
import ast.expressions.OperationNode;
import parser.DataTypes;
import parser.LogError;
import sml.data.Method;

class Extend extends Method<List> {

	public Extend(List list) {
		super(list, "extend", DataTypes.ANY);
		addParameter(new VariableDeclarationNode("listToExtend", DataTypes.ANY));
	}

	@Override
	public List call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
		setArguments(arguments, callFileName, callLine);
		var listToExtend = getBody().getVariableByName("listToExtend").getValue();
		if (!(listToExtend instanceof List))
			new LogError("Can't extend list with something that isn't list:\t" + listToExtend, callFileName, callLine);
		return parent.extend((List) listToExtend);
	}

}
