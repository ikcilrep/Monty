package stdlib.casts;

import java.math.BigInteger;

import parser.MontyException;

public class StringToInt {


	public static BigInteger stringToInt(String str) {
		if (str.matches("[+-]?[0-9]+\\.[0-9]+"))
			return new BigInteger(str.split("\\.")[0]);
		else if (str.matches("[+-]?[0-9]+"))
			return new BigInteger(str);
		else
			new MontyException("Unknown number format for integer type:\t" + str);
		return null;
	}


}
