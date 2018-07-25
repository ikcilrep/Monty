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

public class ToBoolean extends FunctionDeclarationNode {

	public ToBoolean() {
		super("toBoolean", DataTypes.BOOLEAN);
		setBody(new Block(null));
		addParameter(new VariableDeclarationNode("a", DataTypes.ANY));
	}

	public static Object toBoolean(Object a) {
		if (a == null)
			new MontyException("Can't cast void to boolean");
		if (a instanceof BigInteger)
			return IntToBoolean.intToBoolean((BigInteger) a);
		if (a instanceof Float)
			return FloatToBoolean.floatToBoolean((Float) a);
		if (a instanceof Boolean)
			return (Boolean) a;
		if (a instanceof String)
			return StringToBoolean.stringToBoolean((String) a);
		if (a instanceof Array)
			new MontyException("Can't cast array to boolean:\t" + a.toString());
		return null;
	}

	@Override
	public Object call(ArrayList<OperationNode> arguments) {
		setArguments(arguments);
		var a = getBody().getVariableByName("a").getValue();
		return toBoolean(a);
	}

}
