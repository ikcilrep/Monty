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

final class Get extends Method<List> {

	public Get(List list) {
		super(list, "get", DataTypes.ANY);
		addParameter("index", DataTypes.INTEGER);
	}

	@Override
	public Object call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
		setArguments(arguments, callFileName, callLine);
		var index = ((BigInteger) getBody().getVariable("index").getValue()).intValue();
		var length = parent.length();
		if (index >= length || index < 0)
			new LogError("This list doesn't have " + index + " element", callFileName, callLine);
		return parent.get(index);
	}

}
