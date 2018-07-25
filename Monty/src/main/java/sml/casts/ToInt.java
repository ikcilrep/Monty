package sml.casts;

import java.math.BigInteger;
import java.util.ArrayList;

import ast.Block;
import ast.declarations.FunctionDeclarationNode;
import ast.declarations.VariableDeclarationNode;
import ast.expressions.OperationNode;
import parser.DataTypes;
import parser.MontyException;
import sml.data.array.Array;

public class ToInt extends FunctionDeclarationNode {

	public ToInt() {
		super("toInt", DataTypes.INTEGER);
		setBody(new Block(null));
		addParameter(new VariableDeclarationNode("a", DataTypes.ANY));
	}

	public static BigInteger toInt(Object a) {
		if (a == null)
			new MontyException("Can't cast void to integer");
		if (a instanceof BigInteger)
			return (BigInteger) a;
		if (a instanceof Float)
			return FloatToInt.floatToInt((Float) a);
		if (a instanceof Boolean)
			return BooleanToInt.booleanToInt((Boolean) a);
		if (a instanceof String)
			return StringToInt.stringToInt((String) a);
		if (a instanceof Array)
			new MontyException("Can't cast array to integer:\t" + a.toString());
		return null;
	}

	@Override
	public Object call(ArrayList<OperationNode> arguments) {
		setArguments(arguments);
		var a = getBody().getVariableByName("a").getValue();
		return toInt(a);
	}

}
