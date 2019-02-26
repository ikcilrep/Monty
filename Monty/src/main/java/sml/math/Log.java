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
import parser.DataTypes;
import parser.LogError;

public class Log extends FunctionDeclarationNode {
	public Log() {
		super("log", DataTypes.REAL);
		setBody(new Block(null));
		addParameter("a", DataTypes.REAL);
		addParameter("b", DataTypes.REAL);
		addParameter("scale", DataTypes.INTEGER);
	}

	public static int nearestPower(BigDecimal a, BigDecimal b) {
		var result = 0;
		var test = BigDecimal.ONE;
		while (test.compareTo(b) < 0) {
			test = test.multiply(a);
			result += 1;
		}
		return result - 1;
	}

	public static BigDecimal log(BigDecimal a, BigDecimal b, int scale) {
		var x = nearestPower(a, b);
		a = a.setScale(scale, RoundingMode.HALF_UP);
		if (x == -1)
			return BigDecimal.ZERO;
		var y = a.pow(x);
		var z = y.multiply(a);
		if (z.compareTo(b) == 0)
			return BigDecimal.valueOf(x + 1);
		var divider = 1;
		for (int i = 0; i < 15; i++) {
			var yz = y.multiply(z);
			b = b.multiply(b);
			x <<= 1;
			divider <<= 1;
			if (b.compareTo(yz) > 0) {
				x += 1;
				y = yz;
			} else
				y = y.multiply(y);
			z = y.multiply(a);
		}
		return BigDecimal.valueOf(x).divide(BigDecimal.valueOf(divider), scale, RoundingMode.HALF_UP);
	}

	@Override
	public BigDecimal call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
		setArguments(arguments, callFileName, callLine);
		var body = getBody();
		var scale = ((BigInteger) body.getVariable("scale").getValue()).intValue();
		var a = (BigDecimal) body.getVariable("a").getValue();
		var b = (BigDecimal) body.getVariable("b").getValue();
		if (a.compareTo(BigDecimal.ONE) < 0)
			new LogError("a mustn't be lower than one.");
		if (b.compareTo(BigDecimal.ZERO) < 0)
			new LogError("b mustn't be lower than zero.");
		return log(a, b, scale);
	}

}
