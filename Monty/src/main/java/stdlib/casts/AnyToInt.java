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

public class AnyToInt extends FunctionDeclarationNode {

	public AnyToInt() {
		super("anyToInt", DataTypes.INTEGER);
		setBody(new Block(null));
		addParameter(new VariableDeclarationNode("a", DataTypes.ANY));
	}

	@Override
	public Object call(ArrayList<OperationNode> arguments) {
		var a = getBody().getVariableByName("a").getValue();
		if (a instanceof BigInteger)
			return (BigInteger) a;
		if (a instanceof Float)
			return FloatToInt.floatToInt((Float) a);
		if (a instanceof Boolean)
			return BooleanToInt.booleanToInt((Boolean) a);
		if (a instanceof String)
			return StringToInt.stringToInt((String) a);
		if (a instanceof Array || a == null)
			new MontyException("Can't cast this expression to integer:\t" + a.toString());
		return null;
	}

}
