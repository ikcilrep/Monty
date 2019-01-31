package sml.data.array;

import java.util.ArrayList;

import ast.Block;
import ast.declarations.FunctionDeclarationNode;
import ast.expressions.OperationNode;
import parser.DataTypes;

public class NewIterator extends FunctionDeclarationNode {
	Array array;

	public NewIterator(Array array) {
		super("Iterator", DataTypes.ANY);
		setBody(new Block(array));
		this.array = array;
	}

	@Override
	public Object call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
		return new Iterator(array);
	}

}
