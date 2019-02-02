package sml.data.list;

import java.math.BigInteger;
import java.util.ArrayList;

import ast.declarations.VariableDeclarationNode;
import ast.expressions.OperationNode;
import parser.DataTypes;
import sml.data.Method;

class FindLast extends Method<List> {

	public FindLast(List list) {
		super(list, "findLast", DataTypes.INTEGER);
		addParameter(new VariableDeclarationNode("element", DataTypes.ANY));
	}

	@Override
	public BigInteger call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
		setArguments(arguments, callFileName, callLine);
		return BigInteger.valueOf(parent.findLast(getBody().getVariable("element").getValue()));
	}

}
