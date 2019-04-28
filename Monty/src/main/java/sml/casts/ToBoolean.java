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

import java.math.BigInteger;
import java.util.ArrayList;

import ast.Block;
import ast.declarations.FunctionDeclarationNode;
import ast.expressions.OperationNode;
import parser.LogError;
import sml.data.returning.VoidType;
import sml.data.string.StringStruct;

public final class ToBoolean extends FunctionDeclarationNode {

	public static Boolean toBoolean(Object toBeCasted, String callFileName, int callLine) {
		if (toBeCasted instanceof VoidType)
			new LogError("Can't cast void to boolean", callFileName, callLine);
		if (toBeCasted instanceof BigInteger)
			return fromInt((BigInteger) toBeCasted);
		if (toBeCasted instanceof Integer)
			return fromInt((int) toBeCasted);
		if (toBeCasted instanceof Double)
			return fromFloat((double) toBeCasted);
		if (toBeCasted instanceof Boolean)
			return (boolean) toBeCasted;
		if (toBeCasted instanceof StringStruct)
			return fromString((StringStruct) toBeCasted);
		else
			new LogError("Can't cast structure to boolean", callFileName, callLine);
		return null;
	}

	public ToBoolean() {
		super("toBoolean");
		setBody(new Block(null));
		addParameter("toBeCasted");
	}

	@Override
	public Boolean call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
		setArguments(arguments, callFileName, callLine);
		var toBeCasted = getBody().getVariableValue("toBeCasted", callFileName, callLine);
		return toBoolean(toBeCasted, callFileName, callLine);
	}
	
	
	public static Boolean fromInt(BigInteger integer) {
		return integer.compareTo(BigInteger.valueOf(0)) > 0;
	}

	public static Boolean fromInt(int integer) {
		return integer > 0;
	}
	public static Boolean fromFloat(double floating) {
		return floating > 0;
	}
	public static Boolean fromString(StringStruct str) {
		return Boolean.parseBoolean(str.getString());
	}
}
