package sml.data.stack;

import ast.declarations.StructDeclarationNode;

class Iterator extends StructDeclarationNode {
	Stack stack;
	int counter;
	public Iterator(Stack stack) {
		super(stack, "Iterator");
		this.stack = stack;
		counter =stack.top;
		addFunction(new Next(this));
		addFunction(new HasNext(this));

	}

}
