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
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;

import ast.Block;
import ast.declarations.FunctionDeclarationNode;
import ast.expressions.OperationNode;
import parser.DataTypes;
import parser.LogError;

public final class Root extends FunctionDeclarationNode {
	public Root() {
		super("root", DataTypes.FLOAT);
		setBody(new Block(null));
		addParameter("degree", DataTypes.INTEGER);
		addParameter("n", DataTypes.FLOAT);
		addParameter("scale", DataTypes.INTEGER);
	}

	@Override
	public BigDecimal call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
		setArguments(arguments, callFileName, callLine);
		var body = getBody();
		var scale = ((BigInteger) body.getVariable("scale").getValue()).intValue();
		if (scale <= 0)
			new LogError("Scale have to be greater or equals one", callFileName, callLine);
		var mathContext = new MathContext(scale, RoundingMode.DOWN);
		var degree = ((BigInteger) body.getVariable("degree").getValue()).intValue();
		var bigDecimalDegree = new BigDecimal(degree, mathContext);
		var n = ((BigDecimal) body.getVariable("n").getValue());
		var p = BigDecimal.valueOf(scale).movePointLeft(scale);
		if (n.compareTo(BigDecimal.ZERO) < 0)
			new LogError("This root can only be calculated for numbers greater than zero", callFileName, callLine);
		if (n.equals(BigDecimal.ZERO))
			return BigDecimal.ZERO;
		BigDecimal previous = n;
		BigDecimal x = n.divide(bigDecimalDegree, mathContext);
		while (x.subtract(previous).abs().compareTo(p) > 0) {
			previous = x;
			x = BigDecimal.valueOf(degree - 1.0).multiply(x).add(n.divide(x.pow(degree - 1), mathContext))
					.divide(bigDecimalDegree, mathContext);
		}
		return x;
	}

}
