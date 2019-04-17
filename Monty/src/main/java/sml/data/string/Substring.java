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

final class Substring extends FunctionDeclarationNode {

	public Substring(StaticStruct struct) {
		super("substring");
		setBody(new Block(null));
		addParameter("str");
		addParameter("begin");
		addParameter("end");
		struct.addFunction(this);
	}

	@Override
	public String call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
		setArguments(arguments, callFileName, callLine);
		var body = getBody();
		var str = body.getStringVariableValue("str");
		var _begin = body.getVariableValue("begin");
		var _end = body.getVariableValue("end");

		int begin = 0;
		int end = 0;

		if (_begin instanceof Integer)
			begin = (int) _begin;
		else if (_begin instanceof BigInteger) {
			var bigIndex = (BigInteger) _begin;
			if (bigIndex.compareTo(DataTypes.INT_MAX) > 0)
				new LogError("Index have to be less or equals 2^31-1.", callFileName, callLine);
			else if (bigIndex.compareTo(DataTypes.INT_MIN) < 0)
				new LogError("Index have to be greater or equals -2^31.", callFileName, callLine);
			begin = bigIndex.intValue();
		}

		if (_end instanceof Integer)
			end = (int) _end;
		else if (_begin instanceof BigInteger) {
			var bigIndex = (BigInteger) _end;
			if (bigIndex.compareTo(DataTypes.INT_MAX) > 0)
				new LogError("Index have to be less or equals 2^31-1.", callFileName, callLine);
			else if (bigIndex.compareTo(DataTypes.INT_MIN) < 0)
				new LogError("Index have to be greater or equals -2^31.", callFileName, callLine);
			end = bigIndex.intValue();
		}

		if (begin < 0)
			new LogError("Begin can't be negative.", callFileName, callLine);
		if (end > str.length())
			new LogError("End can't be greater than length of list.", callFileName, callLine);
		if (begin > end)
			new LogError("Begin can't be greater or equals to end.", callFileName, callLine);
		return str.substring(begin, end);
	}

}
