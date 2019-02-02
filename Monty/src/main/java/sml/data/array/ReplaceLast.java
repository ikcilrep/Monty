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

import ast.declarations.VariableDeclarationNode;
import ast.expressions.OperationNode;
import parser.DataTypes;
import sml.data.Method;

class ReplaceLast extends Method<Array> {

	public ReplaceLast(Array array) {
		super(array, "replaceLast", DataTypes.ANY);
		addParameter(new VariableDeclarationNode("toBeReplaced", DataTypes.ANY));
		addParameter(new VariableDeclarationNode("replacement", DataTypes.ANY));

	}

	@Override
	public Array call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
		setArguments(arguments, callFileName, callLine);
		var body = getBody();
		return parent.replaceLast(body.getVariable("toBeReplaced").getValue(),
				body.getVariable("replacement").getValue());
	}

}
