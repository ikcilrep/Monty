package stdlib.data.array;

import java.util.ArrayList;

import ast.declarations.FunctionDeclarationNode;
import ast.expressions.OperationNode;
import parser.DataTypes;

public class ArrayOf extends FunctionDeclarationNode {

	public ArrayOf() {
		super("arrayOf", DataTypes.ARRAY);
	}

	@Override
	public Object call(ArrayList<OperationNode> arguments) {
		var result = new Array();
		for (OperationNode argument : arguments)
			result.append(argument.run());
		return result;
	}

}
