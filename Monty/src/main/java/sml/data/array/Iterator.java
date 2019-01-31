package sml.data.array;

import ast.declarations.StructDeclarationNode;

public class Iterator extends StructDeclarationNode {
	int counter = 0;
	Array array;
	public Iterator(Array array) {
		super(array, "Iterator");
		this.array = array;
		addFunction(new Next(this));
		addFunction(new HasNext(this));

	}

}
