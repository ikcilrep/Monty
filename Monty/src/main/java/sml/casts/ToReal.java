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
import sml.data.returning.VoidType;

public final class ToReal extends FunctionDeclarationNode {

	public static BigDecimal toReal(Object a, String callFileName, int callLine) {
		if (a instanceof VoidType)
			new LogError("Can't cast void to real", callFileName, callLine);
		if (a instanceof BigInteger)
			return IntToReal.intToReal((BigInteger) a);
		if (a instanceof Boolean)
			return BooleanToReal.booleanToReal((Boolean) a);
		if (a instanceof BigDecimal)
			return (BigDecimal) a;
		if (a instanceof String)
			return StringToReal.stringToReal((String) a, callFileName, callLine);
		else
			new LogError("Can't cast structure to real", callFileName, callLine);
		return null;
	}

	public ToReal() {
		super("toReal", DataTypes.REAL);
		setBody(new Block(null));
		addParameter("a", DataTypes.ANY);
	}

	@Override
	public BigDecimal call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
		setArguments(arguments, callFileName, callLine);
		var a = getBody().getVariableValue("a");
		return toReal(a, callFileName, callLine);
	}

}
