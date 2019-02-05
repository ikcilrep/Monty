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

public final class Pow extends FunctionDeclarationNode {

	public Pow() {
		super("pow", DataTypes.FLOAT);
		setBody(new Block(null));
		addParameter("basis", DataTypes.FLOAT);
		addParameter("index", DataTypes.INTEGER);
	}

	@Override
	public BigDecimal call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
		setArguments(arguments, callFileName, callLine);
		var body = getBody();
		var basis = (BigDecimal) body.getVariable("basis").getValue();
		var index = ((BigInteger) body.getVariable("index").getValue()).intValue();
		if (index > 999999999)
			new LogError("Index have to be lower than 999999999", callFileName, callLine);
		if (index < 0)
			return BigDecimal.ONE.divide(basis.pow(0 - index));
		return basis.pow(index);
	}

}
