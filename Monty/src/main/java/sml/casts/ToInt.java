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

public final class ToInt extends FunctionDeclarationNode {

	public static Object toInt(Object a, String callFileName, int callLine) {
		if (a instanceof VoidType)
			new LogError("Can't cast void to integer", callFileName, callLine);
		if (a instanceof BigInteger || a instanceof Integer)
			return a;
		if (a instanceof Double)
			return (int) (double) a;
		if (a instanceof Boolean)
			return BooleanToInt.booleanToInt((Boolean) a);
		if (a instanceof String)
			return StringToInt.stringToInt((String) a, callFileName, callLine);
		else
			new LogError("Can't cast structure to integer", callFileName, callLine);
		return null;
	}

	public ToInt() {
		super("toInt");
		setBody(new Block(null));
		addParameter("a");
	}

	@Override
	public Object call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
		setArguments(arguments, callFileName, callLine);
		var a = getBody().getVariableValue("a");
		return toInt(a, callFileName, callLine);
	}

}
