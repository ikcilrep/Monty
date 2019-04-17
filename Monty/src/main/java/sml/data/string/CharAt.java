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
package sml.data.string;

import java.math.BigInteger;
import java.util.ArrayList;

import ast.Block;
import ast.declarations.FunctionDeclarationNode;
import ast.expressions.OperationNode;
import parser.DataTypes;
import parser.LogError;
import sml.data.StaticStruct;

final class CharAt extends FunctionDeclarationNode {

	public CharAt(StaticStruct struct) {
		super("charAt");
		setBody(new Block(null));
		addParameter("str");
		addParameter("index");
		struct.addFunction(this);
	}

	@Override
	public String call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
		setArguments(arguments, callFileName, callLine);
		var body = getBody();
		var str = body.getStringVariableValue("str");
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
		}
		return String.valueOf(str.charAt(index));
	}

}
