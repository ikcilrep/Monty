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
import parser.DataTypes;
import parser.LogError;
import sml.data.array.Array;
import sml.data.stack.Stack;

public class ToFloat extends FunctionDeclarationNode {

	public static BigDecimal toFloat(Object a, String callFileName, int callLine) {
		if (a == null)
			new LogError("Can't cast void to float", callFileName, callLine);
		if (a instanceof BigInteger)
			return IntToFloat.intToFloat((BigInteger) a);
		if (a instanceof Boolean)
			return BooleanToFloat.booleanToFloat((Boolean) a);
		if (a instanceof BigDecimal)
			return (BigDecimal) a;
		if (a instanceof String)
			return StringToFloat.stringToFloat((String) a, callFileName, callLine);
		if (a instanceof Array)
			new LogError("Can't cast array to float:\t" + a.toString(), callFileName, callLine);
		if (a instanceof Stack)
			new LogError("Can't cast stack to float:\t" + a.toString(), callFileName, callLine);
		return null;
	}

	/**
	 * 
	 */

	public ToFloat() {
		super("toFloat", DataTypes.FLOAT);
		setBody(new Block(null));
		addParameter(new VariableDeclarationNode("a", DataTypes.ANY));
	}

	@Override
	public Object call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
		setArguments(arguments, callFileName, callLine);
		var a = getBody().getVariableByName("a").getValue();
		return toFloat(a, callFileName, callLine);
	}

}
