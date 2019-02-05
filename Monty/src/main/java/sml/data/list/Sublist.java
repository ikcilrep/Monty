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

package sml.data.list;

import java.math.BigInteger;
import java.util.ArrayList;

import ast.expressions.OperationNode;
import parser.DataTypes;
import parser.LogError;
import sml.data.Method;

final class Sublist extends Method<List> {

	public Sublist(List list) {
		super(list, "sublist", DataTypes.ANY);
		addParameter("begin", DataTypes.INTEGER);
		addParameter("end", DataTypes.INTEGER);
	}

	@Override
	public Object call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
		setArguments(arguments, callFileName, callLine);
		var body = getBody();
		var begin = ((BigInteger) body.getVariable("begin").getValue()).intValue();
		var end = ((BigInteger) body.getVariable("end").getValue()).intValue();
		var length = parent.length();
		if (begin >= end)
			new LogError("Begin can't be greater or equals than end", callFileName, callLine);
		if (begin < 0 || begin >= length)
			new LogError("This list doesn't have " + begin + " index", callFileName, callLine);
		if (end < 0 || end > length)
			new LogError("This list doesn't have " + end + " index", callFileName, callLine);
		return parent.sublist(begin, end);
	}

}
