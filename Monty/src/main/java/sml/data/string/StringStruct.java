package sml.data.string;

import java.util.HashMap;

import sml.data.StaticStruct;

public final class StringStruct {
	private static StaticStruct struct = new StaticStruct();

	static {
		struct.setFunctions(new HashMap<>());
		new CharAt(struct);
		new EndsWith(struct);
		new EqualsIgnoreCase(struct);
		new Length(struct);
		new LowerCase(struct);
		new Replace(struct);
		new ReplaceFirst(struct);
		new Split(struct);
		new StartsWith(struct);
		new Substring(struct);
		new UpperCase(struct);
		new Iterator(struct);
	}

	public static StaticStruct getStruct() {
		return struct;
	}
}
