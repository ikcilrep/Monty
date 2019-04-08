package sml.iterations;

import java.math.BigInteger;
import java.util.ArrayList;

import ast.Block;
import ast.declarations.FunctionDeclarationNode;
import ast.expressions.OperationNode;
import parser.DataTypes;

public class NewRange extends FunctionDeclarationNode {

	public NewRange() {
		super("Range", DataTypes.ANY);
		setBody(new Block(null));
		addParameter("min", DataTypes.INTEGER);
		addParameter("max", DataTypes.INTEGER);
		addParameter("step", DataTypes.INTEGER);

	}

	@Override
	public Object call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
		setArguments(arguments, callFileName, callLine);
		var body = getBody();
		return new Range((BigInteger) body.getVariable("min").getValue(),
				(BigInteger) body.getVariable("max").getValue(), (BigInteger) body.getVariable("step").getValue());
	}

}
