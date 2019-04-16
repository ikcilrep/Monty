package sml.data.string;

import java.util.HashMap;

import sml.data.StaticStruct;

public final class StringStruct {
	private static StaticStruct struct = new StaticStruct();

	static {
		struct.setFunctions(new HashMap<>());
		new CharAt();
		new EndsWith();
		new EqualsIgnoreCase();
		new Length();
		new LowerCase();
		new Replace();
		new ReplaceFirst();
		new Split();
		new StartsWith();
		new Substring();
		new UpperCase();
	}
	public static StaticStruct getStruct() {
		return struct;
	}
}
