/*
Copyright 2018-2019 Szymon Perlicki

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUObject WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package sml.data.string;

import java.util.ArrayList;

import ast.Block;
import ast.declarations.FunctionDeclarationNode;
import ast.expressions.OperationNode;
import parser.DataTypes;
import sml.data.array.Array;

public class Split extends FunctionDeclarationNode {

	public Split() {
		super("split", DataTypes.ANY);
		setBody(new Block(null));
		addParameter("str", DataTypes.STRING);
		addParameter("regex", DataTypes.STRING);
	}

	@Override
	public Object call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
		setArguments(arguments, callFileName, callLine);
		var body = getBody();
		var str = (String)body.getVariable("str").getValue();
		var regex = (String)body.getVariable("regex").getValue();
		return new Array(str.split(regex));
	}

}
