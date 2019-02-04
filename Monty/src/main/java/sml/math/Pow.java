package sml.math;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;

import ast.Block;
import ast.declarations.FunctionDeclarationNode;
import ast.expressions.OperationNode;
import parser.DataTypes;
import parser.LogError;

public class Pow extends FunctionDeclarationNode {

	public Pow() {
		super("pow", DataTypes.FLOAT);
		setBody(new Block(null));
		addParameter("basis", DataTypes.FLOAT);
		addParameter("index", DataTypes.INTEGER);
	}

	@Override
	public BigDecimal call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
		setArguments(arguments, callFileName, callLine);
		var body = getBody();
		var basis = (BigDecimal) body.getVariable("basis").getValue();
		var index = ((BigInteger) body.getVariable("index").getValue()).intValue();
		if (index > 999999999)
			new LogError("Index have to be lower than 999999999", callFileName, callLine);
		if (index < 0)
			return BigDecimal.ONE.divide(basis.pow(0-index));
		return basis.pow(index);
	}

}
