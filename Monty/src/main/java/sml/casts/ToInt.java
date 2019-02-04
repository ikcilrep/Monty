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
import ast.expressions.OperationNode;
import parser.DataTypes;
import parser.LogError;
import sml.data.array.Array;
import sml.data.stack.Stack;

public class ToInt extends FunctionDeclarationNode {

	public static BigInteger toInt(Object a, String callFileName, int callLine) {
		if (a == null)
			new LogError("Can't cast void to integer", callFileName, callLine);
		if (a instanceof BigInteger)
			return (BigInteger) a;
		if (a instanceof BigDecimal)
			return ((BigDecimal) a).toBigInteger();
		if (a instanceof Boolean)
			return BooleanToInt.booleanToInt((Boolean) a);
		if (a instanceof String)
			return StringToInt.stringToInt((String) a, callFileName, callLine);
		if (a instanceof Array)
			new LogError("Can't cast array to integer:\t" + a.toString(), callFileName, callLine);
		if (a instanceof Stack)
			new LogError("Can't cast stack to integer:\t" + a.toString(), callFileName, callLine);
		return null;
	}

	public ToInt() {
		super("toInt", DataTypes.INTEGER);
		setBody(new Block(null));
		addParameter("a", DataTypes.ANY);
	}

	@Override
	public Object call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
		setArguments(arguments, callFileName, callLine);
		var a = getBody().getVariable("a").getValue();
		return toInt(a, callFileName, callLine);
	}

}
