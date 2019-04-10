package sml.functional.iterable.string;

import ast.declarations.StructDeclarationNode;

public final class IterableString extends StructDeclarationNode {
	char[] string;
	public IterableString(String string) {
		super(null, "iterableString");
		this.string = string.toCharArray();
		new NewIterator(this);
	}

}
