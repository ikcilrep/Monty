package sml.data.array;

import java.math.BigInteger;
import java.util.ArrayList;

import ast.Block;
import ast.declarations.FunctionDeclarationNode;
import ast.declarations.VariableDeclarationNode;
import ast.expressions.OperationNode;
import parser.DataTypes;

public class Subarray extends FunctionDeclarationNode {

	public Subarray() {
		super("subArray", DataTypes.ARRAY);
		setBody(new Block(null));
		addParameter(new VariableDeclarationNode("arr", DataTypes.ARRAY));
		addParameter(new VariableDeclarationNode("begin", DataTypes.INTEGER));
		addParameter(new VariableDeclarationNode("end", DataTypes.INTEGER));

	}

	@Override
	public Object call(ArrayList<OperationNode> arguments) {
		setArguments(arguments);
		var body = getBody();
		var arr = (Array) body.getVariableByName("arr").getValue();
		var begin = ((BigInteger) body.getVariableByName("begin").getValue()).intValue();
		var end = ((BigInteger) body.getVariableByName("end").getValue()).intValue();
		return arr.subArray(begin, end);
	}

}
