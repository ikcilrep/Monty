package sml.math;

import java.util.HashMap;

import sml.data.StaticStruct;

public final class MathStruct{
	private static StaticStruct struct = new StaticStruct();
	static {
		struct.setFunctions(new HashMap<>());
		new Abs();
		new Exp();
		new Factorial();
		new Pow();
		new Round();
		new Scale();
	}
	public static StaticStruct getStruct() {
		return struct;
	}

}
