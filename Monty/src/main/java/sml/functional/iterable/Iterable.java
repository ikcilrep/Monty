package sml.functional.iterable;


import ast.declarations.StructDeclarationNode;
import sml.functional.iterable.string.IterableString;

class Iterable extends StructDeclarationNode{
	StructDeclarationNode iterable;
	public Iterable(Object iterable) {
		super(null, "Iterable");
		incrementNumber();
		if (iterable instanceof String)
			this.iterable = new IterableString((String)iterable);
		else
			this.iterable = (StructDeclarationNode) iterable;
		new NewIterator(this);
	}
	
}
