package sml.data.array;

import java.util.ArrayList;

import ast.declarations.FunctionDeclarationNode;
import ast.expressions.OperationNode;
import parser.DataTypes;

public class Next extends FunctionDeclarationNode {
	Iterator iterator;
	public Next(Iterator iterator) {
		super("next", DataTypes.ANY);
		this.iterator = iterator;
	}

	@Override
	public Object call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
		return iterator.array.get(iterator.counter++);
	}

}
