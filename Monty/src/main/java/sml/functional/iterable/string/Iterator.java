package sml.functional.iterable.string;

import ast.declarations.StructDeclarationNode;

final class Iterator extends StructDeclarationNode{
	int counter = 0;
	char[] string;
	public Iterator(char[] string) {
		super(null, "Iterator");
		this.string = string;
		new HasNext(this);
		new Next(this);
	}
	
}
