package sml.data.stack;

import java.util.ArrayList;

import ast.Block;
import ast.declarations.FunctionDeclarationNode;
import ast.expressions.OperationNode;
import parser.DataTypes;

public class NewIterator extends FunctionDeclarationNode {
	Stack stack;

	public NewIterator(Stack stack) {
		super("Iterator", DataTypes.ANY);
		setBody(new Block(stack));
		this.stack = stack;
	}

	@Override
	public Object call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
		return new Iterator(stack);
	}

}
