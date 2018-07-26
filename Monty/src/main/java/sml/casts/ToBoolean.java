package sml.casts;
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

import java.math.BigInteger;
import java.util.ArrayList;

import ast.Block;
import ast.declarations.FunctionDeclarationNode;
import ast.declarations.VariableDeclarationNode;
import ast.expressions.OperationNode;
import parser.DataTypes;
import parser.MontyException;
import sml.data.array.Array;

public class ToBoolean extends FunctionDeclarationNode {

	public ToBoolean() {
		super("toBoolean", DataTypes.BOOLEAN);
		setBody(new Block(null));
		addParameter(new VariableDeclarationNode("a", DataTypes.ANY));
	}

	public static Object toBoolean(Object a) {
		if (a == null)
			new MontyException("Can't cast void to boolean");
		if (a instanceof BigInteger)
			return IntToBoolean.intToBoolean((BigInteger) a);
		if (a instanceof Float)
			return FloatToBoolean.floatToBoolean((Float) a);
		if (a instanceof Boolean)
			return (Boolean) a;
		if (a instanceof String)
			return StringToBoolean.stringToBoolean((String) a);
		if (a instanceof Array)
			new MontyException("Can't cast array to boolean:\t" + a.toString());
		return null;
	}

	@Override
	public Object call(ArrayList<OperationNode> arguments) {
		setArguments(arguments);
		var a = getBody().getVariableByName("a").getValue();
		return toBoolean(a);
	}

}
