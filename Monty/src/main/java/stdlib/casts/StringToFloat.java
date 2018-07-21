package stdlib.casts;


import parser.MontyException;

public class StringToFloat {



	public static Float stringToFloat(String str) {
		if (str.matches("[+-]?[0-9]+\\.[0-9]+"))
			return Float.parseFloat(str);
		else if (str.matches("[+-]?[0-9]+"))
			return (Float) (float) Integer.parseInt(str);
		else
			new MontyException("Unknown number format for float type:\t" + str);
		return null;
	}


}
