package stdlib.system;

import java.util.ArrayList;
import ast.declarations.FunctionDeclarationNode;
import ast.expressions.OperationNode;
import parser.DataTypes;

public class Exit extends FunctionDeclarationNode {

	public Exit() {
		super("exit", DataTypes.VOID);
	}

	@Override
	public Object call(ArrayList<OperationNode> arguments) {
		setArguments(arguments);
		System.exit(0);
		return null;
	}

}
