package parser;

import java.math.BigInteger;

public enum DataTypes {
	BOOLEAN,
	INTEGER,
	FLOAT,
	STRING,
	VOID;

	public static DataTypes getDataType(Object value) {
	
		if (value == null)
			return DataTypes.VOID;
		if (value instanceof BigInteger)
			return DataTypes.INTEGER;
		if (value instanceof Float)
			return DataTypes.FLOAT;
		if (value instanceof String)
			return DataTypes.STRING;
		if (value instanceof Boolean)
			return DataTypes.BOOLEAN;
		return null;
	
	}
}
