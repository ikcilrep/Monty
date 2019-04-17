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

package sml.math;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;

import ast.Block;
import ast.declarations.FunctionDeclarationNode;
import ast.expressions.OperationNode;
import parser.DataTypes;
import parser.LogError;
import sml.data.StaticStruct;

public final class Pow extends FunctionDeclarationNode {
	public Pow(StaticStruct struct) {
		super("pow");
		setBody(new Block(null));
		addParameter("basis");
		addParameter("index");
		struct.addFunction(this);
	}

	private Object pow(int basis, int index) {
		var result = basis;
		for (int i = 1; i < index; i++)
			try {
				result = Math.multiplyExact(result, basis);
			} catch (ArithmeticException e) {
				return bigMultiply(BigInteger.valueOf(result), bigPow(BigInteger.valueOf(basis), index - i));
			}
		return result;
	}

	private Object bigPow(BigInteger basis, int index) {
		if (index < 0)
			return BigDecimal.ONE.divide(new BigDecimal(((BigInteger) basis).pow(-index))).doubleValue();
		return ((BigInteger) basis).pow(index);
	}

	private Object bigMultiply(BigInteger a, Object b) {
		if (b instanceof Double)
			return BigDecimal.valueOf((double) b).multiply(new BigDecimal(a)).doubleValue();
		return a.multiply((BigInteger) b);
	}

	@Override
	public Object call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
		setArguments(arguments, callFileName, callLine);
		var body = getBody();
		var basis = body.getVariableValue("basis");
		var _index = body.getVariableValue("index");
		int index = 0;
		if (_index instanceof Integer)
			index = (int) _index;
		else if (_index instanceof BigInteger) {
			var bigIndex = (BigInteger) _index;
			if (bigIndex.compareTo(DataTypes.INT_MAX) > 0)
				new LogError("Index have to be less or equals 2^31-1.", callFileName, callLine);
			else if (bigIndex.compareTo(DataTypes.INT_MIN) < 0)
				new LogError("Index have to be greater or equals -2^31.", callFileName, callLine);

			index = bigIndex.intValue();
		} else if (_index instanceof Double) {
			if (basis instanceof Double)
				return Math.pow((double) basis, (double) _index);
			else if (basis instanceof Integer)
				return Math.pow(Double.valueOf((int) basis), (double) _index);
			else if (basis instanceof BigInteger)
				return Math.pow(((BigInteger) basis).doubleValue(), (double) _index);
		}

		if (basis instanceof Integer)
			return pow((int) basis, index);
		else if (basis instanceof BigInteger)
			return bigPow((BigInteger) basis, index);
		else if (basis instanceof Double)
			return Math.pow((double) basis, index);

		return null;
	}

}
