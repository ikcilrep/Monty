/*
Copyright 2018-2019 Szymon Perlicki

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUObject WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package sml.math;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;

import ast.Block;
import ast.declarations.FunctionDeclarationNode;
import ast.expressions.OperationNode;
import sml.data.StaticStruct;

public final class Exp extends FunctionDeclarationNode {
	private final static int SCALE = 100;
	private final static BigDecimal PRECISION = BigDecimal.valueOf(100);

	public Exp(StaticStruct struct) {
		super("exp");
		setBody(new Block(null));
		addParameter("x");
		struct.addFunction(this);
	}

	public final static BigDecimal exp(BigDecimal x) {
		var result = x.add(BigDecimal.ONE);
		var a = x;
		var b = BigDecimal.ONE;
		for (BigDecimal i = BigDecimal.valueOf(2); i.compareTo(PRECISION) <= 0; i = i.add(BigDecimal.ONE)) {
			b = b.multiply(i);
			a = a.multiply(x);
			result = result.add(a.divide(b, SCALE, RoundingMode.HALF_UP));
		}
		return result;
	}

	@Override
	public Object call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
		setArguments(arguments, callFileName, callLine);
		var x = getBody().getVariableValue("x");
		if (x instanceof Double)
			return Math.exp((double) x);
		else if (x instanceof Integer)
			return Math.exp(Double.valueOf((int) x));
		else if (x instanceof BigInteger)
			return Math.exp(((BigInteger) x).doubleValue());

		return null;
	}

}
