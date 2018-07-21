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

public class AnyToString extends FunctionDeclarationNode {

	public AnyToString() {
		super("anyToString", DataTypes.STRING);
		setBody(new Block(null));
		addParameter(new VariableDeclarationNode("a", DataTypes.ANY));
	}

	@Override
	public Object call(ArrayList<OperationNode> arguments) {
		var a = getBody().getVariableByName("a").getValue();
		if (a instanceof BigInteger)
			return IntToString.intToString((BigInteger) a);
		if (a instanceof Float)
			return FloatToString.floatToString((Float) a);
		if (a instanceof Boolean)
			return BooleanToString.booleanToString((Boolean) a);
		if (a instanceof String)
			return a.toString();
		if (a instanceof Array || a == null)
			new MontyException("Can't cast this expression to integer:\t" + a.toString());
		return null;
	}

}
