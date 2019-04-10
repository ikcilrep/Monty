package sml.iterations.range;

import java.math.BigInteger;

import ast.declarations.StructDeclarationNode;

class Range extends StructDeclarationNode{
	BigInteger min;
	BigInteger max;
	BigInteger step;
	
	public Range(BigInteger min, BigInteger max, BigInteger step) {
		super(null, "Range");
		this.min = min;
		this.max = max;
		this.step = step;
		new NewIterator(this);
		new Get(this);
	}
	
	public BigInteger get(BigInteger index) {
		return min.add(step.multiply(index));
	}

}