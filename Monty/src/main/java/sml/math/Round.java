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
import java.math.RoundingMode;
import java.util.ArrayList;

import ast.Block;
import ast.declarations.FunctionDeclarationNode;
import ast.expressions.OperationNode;
import parser.DataTypes;

public final class Round extends FunctionDeclarationNode {

	public Round() {
		super("round", DataTypes.REAL);
		setBody(new Block(null));
		addParameter("f", DataTypes.REAL);
		addParameter("scale", DataTypes.INTEGER);

	}

	@Override
	public BigDecimal call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
		setArguments(arguments, callFileName, callLine);
		var body = getBody();
		var f = ((BigDecimal) body.getVariable("f").getValue());
		return f.setScale(((BigInteger) body.getVariable("scale").getValue()).intValue(), RoundingMode.HALF_UP);
	}

}
