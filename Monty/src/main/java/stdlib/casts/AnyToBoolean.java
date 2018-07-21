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

public class AnyToBoolean extends FunctionDeclarationNode {

	public AnyToBoolean() {
		super("anyToBoolean", DataTypes.BOOLEAN);
		setBody(new Block(null));
		addParameter(new VariableDeclarationNode("a", DataTypes.ANY));
	}

	@Override
	public Object call(ArrayList<OperationNode> arguments) {
		if (arguments.size() > 1)
			new MontyException("Too many arguments in " + name + " function call");
		else if (arguments.size() < 1)
			new MontyException("Too few arguments in " + name + " function call");
		var a = arguments.get(0).run();
		if (a instanceof BigInteger)
			return IntToBoolean.intToBoolean((BigInteger) a);
		if (a instanceof Float)
			return FloatToBoolean.floatToBoolean((Float) a);
		if (a instanceof Boolean)
			return (Boolean) a;
		if (a instanceof String)
			return StringToBoolean.stringToBoolean((String) a);
		if (a instanceof Array || a == null)
			new MontyException("Can't cast this expression to integer:\t" + a.toString());
		return null;
	}

}
