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

import java.util.ArrayList;

import ast.expressions.OperationNode;
import parser.DataTypes;
import parser.LogError;
import sml.data.Method;

final class Extend extends Method<List> {

	public Extend(List list) {
		super(list, "extend", DataTypes.ANY);
		addParameter("listToExtend", DataTypes.ANY);
	}

	@Override
	public List call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
		setArguments(arguments, callFileName, callLine);
		var listToExtend = getBody().getVariable("listToExtend").getValue();
		if (!(listToExtend instanceof List))
			new LogError("Can't extend list with something that isn't list:\t" + listToExtend, callFileName, callLine);
		return parent.extend((List) listToExtend);
	}

}
