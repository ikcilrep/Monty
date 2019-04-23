package sml.data.string;

import ast.declarations.StructDeclarationNode;

final class Iterator extends StructDeclarationNode {
	int counter = 0;
	char[] string;

	public Iterator(StringStruct stringStruct) {
		super(null, "Iterator");
		this.string = stringStruct.getString().toCharArray();
		new HasNext(this);
		new Next(this);
	}

}
