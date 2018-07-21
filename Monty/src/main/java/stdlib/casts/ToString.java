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

	@Override
	public Object call(ArrayList<OperationNode> arguments) {
		var a = getBody().getVariableByName("a").getValue();
		if (a == null)
			new MontyException("Can't cast void to integer.");
		return a.toString();
	}

}
