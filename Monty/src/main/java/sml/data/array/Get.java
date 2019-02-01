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

package sml.data.array;

import java.math.BigInteger;
import java.util.ArrayList;
import ast.declarations.VariableDeclarationNode;
import ast.expressions.OperationNode;
import parser.DataTypes;
import parser.LogError;
import sml.data.Method;

class Get extends Method<Array> {


	public Get(Array array) {
		super(array,"get", DataTypes.ANY);
		addParameter(new VariableDeclarationNode("index", DataTypes.INTEGER));
	}

	@Override
	public Object call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
		setArguments(arguments, callFileName, callLine);
		var index = (BigInteger) getBody().getVariableByName("index").getValue();
		if (index.compareTo(BigInteger.valueOf(parent.length())) >= 0)
			new LogError("Index " + index + " is too large for length " + parent.length(), callFileName, callLine);
		return parent.get(index.intValue());
	}

}