package sml.data.array;

import java.util.ArrayList;

import ast.declarations.FunctionDeclarationNode;
import ast.expressions.OperationNode;
import parser.DataTypes;

public class HasNext extends FunctionDeclarationNode {
	Iterator iterator;
	public HasNext(Iterator iterator) {
		super("hasNext", DataTypes.BOOLEAN);
		this.iterator = iterator;
	}

	@Override
	public Object call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
		return iterator.counter < iterator.array.length();
	}

}
