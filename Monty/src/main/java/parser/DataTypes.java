package parser;

import java.math.BigInteger;


import sml.data.array.Array;
import sml.data.returning.VoidType;

public enum DataTypes {
	BOOLEAN, INTEGER, FLOAT, STRING, VOID, ARRAY, ANY;

	public static DataTypes getDataType(Object value) {

		if (value instanceof VoidType)
			return DataTypes.VOID;
		if (value instanceof BigInteger)
			return DataTypes.INTEGER;
		if (value instanceof Float)
			return DataTypes.FLOAT;
		if (value instanceof String)
			return DataTypes.STRING;
		if (value instanceof Boolean)
			return DataTypes.BOOLEAN;
		if (value instanceof Array)
			return DataTypes.ARRAY;
		return null;

	}
}
