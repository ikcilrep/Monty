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

import java.util.ArrayList;

import ast.Block;
import ast.declarations.FunctionDeclarationNode;
import ast.expressions.OperationNode;
import parser.DataTypes;

public final class StartsWith extends FunctionDeclarationNode {

	public StartsWith() {
		super("startsWith", DataTypes.BOOLEAN);
		setBody(new Block(null));
		addParameter("str", DataTypes.STRING);
		addParameter("prefix", DataTypes.STRING);

	}

	@Override
	public Object call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
		setArguments(arguments, callFileName, callLine);
		var body = getBody();
		var str = (String) body.getVariable("str").getValue();
		var prefix = (String) body.getVariable("prefix").getValue();
		return str.startsWith(prefix);
	}

}
