package sml.casts;

public class BooleanToFloat {

	public static Float booleanToFloat(Boolean bool) {
		if (bool == true)
			return (Float) 1f;
		return (Float) 0f;
	}

}
