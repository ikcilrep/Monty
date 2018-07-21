package stdlib.casts;

import java.math.BigInteger;

public class IntToFloat {


	public static Float intToFloat(BigInteger integer) {
		return (Float) (float) integer.intValue();

	}

}
