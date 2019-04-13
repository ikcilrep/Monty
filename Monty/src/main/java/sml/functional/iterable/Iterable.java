package sml.functional.iterable;

import ast.declarations.StructDeclarationNode;
import ast.declarations.VariableDeclarationNode;
import parser.DataTypes;
import sml.functional.iterable.string.IterableString;

final class Iterable extends StructDeclarationNode {
	StructDeclarationNode iterable;

	public Iterable(Object iterable) {
		super(null, "Iterable");
		incrementNumber();
		if (iterable instanceof String)
			this.iterable = new IterableString((String) iterable);
		else
			this.iterable = (StructDeclarationNode) iterable;
		var _this = new VariableDeclarationNode("This", DataTypes.ANY);
		addVariable(_this);
		_this.setValue(this);
		_this.setConst(true);
		new Get(this);
		new NewIterator(this);
		new ToList(this);
	}

}
