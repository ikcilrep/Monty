package sml.functional.iterable;

import ast.declarations.StructDeclarationNode;

class Iterator extends StructDeclarationNode{
	StructDeclarationNode iterator;
	public Iterator(StructDeclarationNode iterator) {
		super(null, "Iterator");
		this.iterator = iterator;
		new HasNext(this);
		new Next(this);
	}
}
