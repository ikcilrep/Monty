package stdlib.casts;

import java.math.BigInteger;
import java.util.ArrayList;

import ast.Block;
import ast.declarations.FunctionDeclarationNode;
import ast.declarations.VariableDeclarationNode;
import ast.expressions.OperationNode;
import parser.DataTypes;
import parser.MontyException;
import stdlib.data.array.Array;

public class ToFloat extends FunctionDeclarationNode {

	public ToFloat() {
		super("toFloat", DataTypes.BOOLEAN);
		setBody(new Block(null));
		addParameter(new VariableDeclarationNode("a", DataTypes.ANY));
	}
	public static Object toFloat(Object a) {
		if (a == null)
			new MontyException("Can't cast void to float");
		if (a instanceof BigInteger)
			return IntToFloat.intToFloat((BigInteger) a);
		if (a instanceof Boolean)
			return BooleanToFloat.booleanToFloat((Boolean) a);
		if (a instanceof Float)
			return (Float) a;
		if (a instanceof String)
			return StringToFloat.stringToFloat((String) a);
		if (a instanceof Array)
			new MontyException("Can't cast array to float:\t" + a.toString());
		return null;
	}
	
	@Override
	public Object call(ArrayList<OperationNode> arguments) {
		setArguments(arguments);
		var a = getBody().getVariableByName("a").getValue();
		return toFloat(a);
	}

}
