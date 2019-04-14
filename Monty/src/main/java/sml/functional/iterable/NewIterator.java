package sml.functional.iterable;

import java.util.ArrayList;

import ast.declarations.StructDeclarationNode;
import ast.expressions.OperationNode;
import parser.DataTypes;
import sml.Sml;
import sml.data.Method;

final class NewIterator extends Method<Iterable> {

	public NewIterator(Iterable parent) {
		super(parent, "Iterator", DataTypes.ANY);
	}

	@Override
	public Iterator call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
		return new Iterator((StructDeclarationNode) parent.iterable.getFunction("Iterator").call(Sml.emptyArgumentList,
				callFileName, callLine));
	}

}
