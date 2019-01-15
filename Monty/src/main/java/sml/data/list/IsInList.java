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

import ast.Block;
import ast.declarations.FunctionDeclarationNode;
import ast.declarations.VariableDeclarationNode;
import ast.expressions.OperationNode;
import parser.DataTypes;

public class IsInList extends FunctionDeclarationNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7038852480570338076L;

	public IsInList() {
		super("isInList", DataTypes.BOOLEAN);
		setBody(new Block(null));
		addParameter(new VariableDeclarationNode("lst", DataTypes.LIST));
		addParameter(new VariableDeclarationNode("element", DataTypes.ANY));

	}

	@Override
	public Object call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
		setArguments(arguments, callFileName, callLine);
		var body = getBody();
		var lst = (List) body.getVariableByName("lst").getValue();
		var element = body.getVariableByName("element").getValue();

		return lst.contains(element);
	}

}
