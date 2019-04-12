package sml.iterations.range;

import java.math.BigInteger;

import ast.declarations.StructDeclarationNode;

final class Iterator extends StructDeclarationNode {
	BigInteger counter;
	BigInteger max;
	BigInteger step;

	public Iterator(Range range) {
		super(range, "Iterator");
		counter = range.min;
		step = range.step;
		max = range.max;
		new Next(this);
		new HasNext(this);
	}

}
