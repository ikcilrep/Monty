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
import parser.LogError;
import sml.data.returning.VoidType;

public final class ToBoolean extends FunctionDeclarationNode {

	public static Boolean toBoolean(Object a, String callFileName, int callLine) {
		if (a instanceof VoidType)
			new LogError("Can't cast void to boolean", callFileName, callLine);
		if (a instanceof BigInteger)
			return IntToBoolean.intToBoolean((BigInteger) a);
		if (a instanceof BigDecimal)
			return RealToBoolean.realToBoolean((BigDecimal) a);
		if (a instanceof Boolean)
			return (boolean) a;
		if (a instanceof String)
			return StringToBoolean.stringToBoolean((String) a);
		else
			new LogError("Can't cast structure to boolean", callFileName, callLine);
		return null;
	}

	public ToBoolean() {
		super("toBoolean");
		setBody(new Block(null));
		addParameter("a");
	}

	@Override
	public Boolean call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
		setArguments(arguments, callFileName, callLine);
		var a = getBody().getVariableValue("a");
		return toBoolean(a, callFileName, callLine);
	}

}
