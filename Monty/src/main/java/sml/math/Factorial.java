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

import java.math.BigInteger;
import java.util.ArrayList;

import ast.Block;
import ast.declarations.FunctionDeclarationNode;
import ast.expressions.OperationNode;
import parser.DataTypes;
import parser.LogError;;
public final class Factorial extends FunctionDeclarationNode {

	public Factorial() {
		super("factorial", DataTypes.INTEGER);
		setBody(new Block(null));
		addParameter("n", DataTypes.INTEGER);
	}

	@Override
	public BigInteger call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
		setArguments(arguments, callFileName, callLine);
		var n = (BigInteger)getBody().getVariable("n").getValue();
		if (n.equals(BigInteger.ZERO))
			return BigInteger.ONE;
		if (n.compareTo(BigInteger.ZERO) < 0)
			new LogError("Factorial can only be calculated with positive n");
		var result = BigInteger.ONE;
		for (BigInteger i =BigInteger.TWO; i.compareTo(n) <= 0; i = i.add(BigInteger.ONE)) {
			result = result.multiply(i);
		}
		return result;
	}

}
