package stdlib.casts;

import java.math.BigInteger;


public class IntToBoolean{



	public static Boolean intToBoolean(BigInteger integer) {
		return (Boolean) (integer.compareTo(BigInteger.valueOf(0)) > 0);
	}


}
