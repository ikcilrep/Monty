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


import ast.expressions.OperationNode;
import parser.DataTypes;
import parser.LogError;
import sml.data.Method;

final class Substring extends Method<StringStruct> {

	public Substring(StringStruct parent) {
		super(parent,"substring");
		addParameter("begin");
		addParameter("end");
	}

	@Override
	public String call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
		setArguments(arguments, callFileName, callLine);
		var body = getBody();
		var _begin = body.getVariableValue("begin", callFileName, callLine);
		var _end = body.getVariableValue("end", callFileName, callLine);

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
		if (end > parent.getString().length())
			new LogError("End can't be greater than length of list.", callFileName, callLine);
		if (begin > end)
			new LogError("Begin can't be greater or equals to end.", callFileName, callLine);
		return parent.getString().substring(begin, end);
	}

}
