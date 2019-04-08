package sml.iterations;

import java.math.BigInteger;

import ast.Block;
import ast.declarations.StructDeclarationNode;

class Range extends StructDeclarationNode{
	BigInteger min;
	BigInteger max;
	BigInteger step;
	
	public Range(BigInteger min, BigInteger max, BigInteger step) {
		super(new Block(null), "Range");
		this.min = min;
		this.max = max;
		this.step = step;
		new NewIterator(this);
	}

}
