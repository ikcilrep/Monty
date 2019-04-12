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

import java.util.ArrayList;

import ast.expressions.OperationNode;
import parser.DataTypes;
import parser.LogError;
import sml.data.Method;

final class AssignAdd extends Method<Array> {

	public AssignAdd(Array array) {
		super(array, "$a_add", DataTypes.ANY);
		addParameter("this", DataTypes.ANY);
		addParameter("other", DataTypes.ANY);
	}

	@Override
	public Array call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
		setArguments(arguments, callFileName, callLine);
		var other = getBody().getVariable("other").getValue();
		if (!(other instanceof Array))
			new LogError("Can't extend array with something that isn't array:\t" + other, callFileName, callLine);
		if (other == parent)
			other = new Array(((Array) other).array);
		return parent.extend((Array) other);
	}

}
