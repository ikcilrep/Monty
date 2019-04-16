package sml.iterations.range;

import java.math.BigInteger;
import java.util.ArrayList;

import ast.Block;
import ast.declarations.FunctionDeclarationNode;
import ast.expressions.OperationNode;
import parser.LogError;

public final class NewRange extends FunctionDeclarationNode {

	public NewRange() {
		super("Range");
		setBody(new Block(null));
		addParameter("min");
		addParameter("max");
		addParameter("step");

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
