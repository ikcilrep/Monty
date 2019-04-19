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
import sml.data.StaticStruct;

public final class Exp extends FunctionDeclarationNode {
	public Exp(StaticStruct struct) {
		super("exp");
		setBody(new Block(null));
		addParameter("x");
		struct.addFunction(this);
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
