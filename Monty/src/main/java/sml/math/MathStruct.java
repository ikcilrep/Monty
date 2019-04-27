package sml.math;

import java.util.HashMap;

import sml.data.StaticStruct;

public final class MathStruct {
	private static StaticStruct struct = new StaticStruct();
	static {
		struct.setFunctions(new HashMap<>());
		new Abs(struct);
		new Exp(struct);
		new Round(struct);
		new Factorial(struct);
	}

	public static StaticStruct getStruct() {
		return struct;
	}

}
