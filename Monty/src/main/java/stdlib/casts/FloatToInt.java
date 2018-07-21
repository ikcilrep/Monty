package stdlib.casts;

import java.math.BigInteger;

public class FloatToInt {

	public static BigInteger floatToInt(Float floating) {
		return new BigInteger(floating.toString());

	}

}
