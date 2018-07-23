package stdlib.data.returning;

import java.util.ArrayList;

import ast.declarations.FunctionDeclarationNode;
import ast.expressions.OperationNode;
import parser.DataTypes;

public class Nothing extends FunctionDeclarationNode {
	public static VoidType nothing = new VoidType();
	public Nothing() {
		super("nothing", DataTypes.VOID);
	}

	@Override
	public Object call(ArrayList<OperationNode> arguments) {
		return nothing;
	}
	
}
