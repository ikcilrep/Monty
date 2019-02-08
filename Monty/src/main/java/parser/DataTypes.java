/*
Copyright 2018-2019 Szymon Perlicki

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package parser;

import java.math.BigDecimal;
import java.math.BigInteger;

import ast.declarations.StructDeclarationNode;
import sml.data.returning.Nothing;
import sml.data.returning.VoidType;

public enum DataTypes {
	BOOLEAN, INTEGER, REAL, STRING, VOID, ANY;
	public final static DataTypes getDataType(Object value) {
		if (value instanceof VoidType)
			return DataTypes.VOID;
		if (value instanceof BigInteger)
			return DataTypes.INTEGER;
		if (value instanceof BigDecimal)
			return DataTypes.REAL;
		if (value instanceof String)
			return DataTypes.STRING;
		if (value instanceof Boolean)
			return DataTypes.BOOLEAN;
		if (value instanceof StructDeclarationNode)
			return DataTypes.ANY;
		return null;

	}

	public final static Object getNeutralValue(DataTypes dataType) {
		switch (dataType) {
		case INTEGER:
			return BigInteger.ZERO;
		case REAL:
			return BigDecimal.ZERO;
		case STRING:
			return "";
		case BOOLEAN:
			return false;
		default:
			return Nothing.nothing;
		}
	}
}
