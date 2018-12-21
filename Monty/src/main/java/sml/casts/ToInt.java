/*
Copyright 2018 Szymon Perlicki

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
import ast.declarations.VariableDeclarationNode;
import ast.expressions.OperationNode;
import parser.DataTypes;
import parser.LogError;
import sml.data.array.Array;

public class ToInt extends FunctionDeclarationNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9063163249840188097L;

	public ToInt() {
		super("toInt", DataTypes.INTEGER);
		setBody(new Block(null));
		addParameter(new VariableDeclarationNode("a", DataTypes.ANY));
	}

	public static BigInteger toInt(Object a) {
		if (a == null)
			new LogError("Can't cast void to integer");
		if (a instanceof BigInteger)
			return (BigInteger) a;
		if (a instanceof Float)
			return FloatToInt.floatToInt((Float) a);
		if (a instanceof Boolean)
			return BooleanToInt.booleanToInt((Boolean) a);
		if (a instanceof String)
			return StringToInt.stringToInt((String) a);
		if (a instanceof Array)
			new LogError("Can't cast array to integer:\t" + a.toString());
		return null;
	}

	@Override
	public Object call(ArrayList<OperationNode> arguments) {
		setArguments(arguments);
		var a = getBody().getVariableByName("a").getValue();
		return toInt(a);
	}

}
