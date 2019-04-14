package sml.iterations.range;

import java.math.BigInteger;
import java.util.ArrayList;

import ast.Block;
import ast.declarations.FunctionDeclarationNode;
import ast.expressions.OperationNode;
import parser.DataTypes;
import parser.LogError;

public final class NewRange extends FunctionDeclarationNode {

	public NewRange() {
		super("Range", DataTypes.ANY);
		setBody(new Block(null));
		addParameter("min", DataTypes.INTEGER);
		addParameter("max", DataTypes.INTEGER);
		addParameter("step", DataTypes.INTEGER);

	}

	@Override
	public Range call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
		setArguments(arguments, callFileName, callLine);
		var body = getBody();
		var min = (BigInteger) body.getVariable("min").getValue();
		var max = (BigInteger) body.getVariable("max").getValue();
		if (max.compareTo(min) <= 0)
			new LogError("Maximum can't be greater or equals minimum.", callFileName, callLine);
		return new Range(min, max, (BigInteger) body.getVariable("step").getValue());
	}

}
