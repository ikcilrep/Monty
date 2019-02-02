package sml.data.stack;

import ast.declarations.StructDeclarationNode;

class Iterator extends StructDeclarationNode {
	Stack stack;
	int counter;

	public Iterator(Stack stack) {
		super(stack, "Iterator");
		this.stack = stack;
		counter = stack.top;
		new Next(this);
		new HasNext(this);

	}

}
