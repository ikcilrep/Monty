package sml.casts;

import java.math.BigInteger;

public class BooleanToInt{


	public static BigInteger booleanToInt(Boolean bool) {
		if (bool == true)
			return BigInteger.valueOf(1);
		return BigInteger.valueOf(0);
	}

}
