package stdlib;

import java.util.ArrayList;
import ast.declarations.FunctionDeclarationNode;
import ast.expressions.OperationNode;
import parser.DataTypes;

public class Exit extends FunctionDeclarationNode {

	public Exit() {
		super("exit", DataTypes.INTEGER);
	}

	@Override
	public Object call(ArrayList<OperationNode> arguments) {
		System.exit(0);
		return 0;
	}

}
