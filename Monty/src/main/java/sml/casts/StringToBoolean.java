package sml.casts;


import parser.MontyException;

public class StringToBoolean {

	public static Boolean stringToBoolean(String str) {
		Boolean doesstrEqualsTrue = str.equalsIgnoreCase("true");
		if (doesstrEqualsTrue || str.equalsIgnoreCase("false"))
			return doesstrEqualsTrue;
		else
			new MontyException("Unknown format for boolean type:\t" + str);
		return null;
	}

}
