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

package sml.casts;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;

import ast.Block;
import ast.declarations.FunctionDeclarationNode;
import ast.declarations.VariableDeclarationNode;
import ast.expressions.OperationNode;
import parser.LogError;
import sml.data.returning.VoidType;
import sml.data.string.StringStruct;

public final class ToInt extends FunctionDeclarationNode {

	public static Object toInt(Object toBeCasted, String callFileName, int callLine) {
		if (toBeCasted instanceof VoidType)
			new LogError("Can't cast void to integer", callFileName, callLine);
		if (toBeCasted instanceof BigInteger || toBeCasted instanceof Integer)
			return toBeCasted;
		if (toBeCasted instanceof Double)
			return fromFloat((double) toBeCasted);
		if (toBeCasted instanceof Boolean)
			return fromBoolean((Boolean) toBeCasted);
		if (toBeCasted instanceof StringStruct)
			return fromString((StringStruct) toBeCasted, callFileName, callLine);
		else
			new LogError("Can't cast structure to integer", callFileName, callLine);
		return null;
	}

	public ToInt() {
		super("toInt");
		setBody(new Block(null));
		addParameter("toBeCasted");
	}

	@Override
	public Object call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
		setArguments(arguments, callFileName, callLine);
		var toBeCasted = getBody().getVariableValue("toBeCasted");
		return toInt(toBeCasted, callFileName, callLine);
	}

	public static Object fromString(StringStruct str, String fileName, int line) {
		try {
			return Integer.parseInt(str.getString());
		} catch (NumberFormatException e) {
			return new BigInteger(str.getString());
		}
	}

	public static Object fromFloat(Double floating) {
		try {
			return Integer.parseInt(((Integer) floating.intValue()).toString());
		} catch (ArithmeticException e) {
			return new BigDecimal(floating).toBigInteger();
		}
	}

	public static int fromBoolean(Boolean bool) {
		if (bool == true)
			return 1;
		return 0;
	}

	public static BigInteger fromSmallInt(int integer) {
		return BigInteger.valueOf(integer);
	}

	public static void fromSmallIntVariable(VariableDeclarationNode variable, String fileName, int line) {
		variable.setValue(fromSmallInt((int) variable.getValue()), fileName, line);
	}
}
