package stdlib.casts;

import java.util.ArrayList;

import ast.Block;
import ast.declarations.FunctionDeclarationNode;
import ast.declarations.VariableDeclarationNode;
import ast.expressions.OperationNode;
import parser.DataTypes;
import parser.MontyException;

public class ToString extends FunctionDeclarationNode {

	public ToString() {
		super("toString", DataTypes.STRING);
		setBody(new Block(null));
		addParameter(new VariableDeclarationNode("a", DataTypes.ANY));
	}
	public static String toString(Object a) {
		if (a == null)
			new MontyException("Can't cast void to string.");
		return a.toString();
	}
	@Override
	public Object call(ArrayList<OperationNode> arguments) {
		setArguments(arguments);
		var a = getBody().getVariableByName("a").getValue();
		return toString(a);
	}

}
