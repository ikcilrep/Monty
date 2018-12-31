/*
Copyright 2018 Szymon Perlicki

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
import ast.declarations.VariableDeclarationNode;
import ast.expressions.OperationNode;
import parser.DataTypes;

public class StartsWith extends FunctionDeclarationNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2142637919242098762L;

	public StartsWith() {
		super("startsWith", DataTypes.BOOLEAN);
		setBody(new Block(null));
		addParameter(new VariableDeclarationNode("str", DataTypes.STRING));
		addParameter(new VariableDeclarationNode("prefix", DataTypes.STRING));

	}

	@Override
	public Object call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
		setArguments(arguments, callFileName, callLine);
		var body = getBody();
		var str = (String) body.getVariableByName("str").getValue();
		var prefix = (String) body.getVariableByName("prefix").getValue();
		return str.startsWith(prefix);
	}

}
