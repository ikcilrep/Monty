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

public class ExtendList extends FunctionDeclarationNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4407198079905020979L;

	public ExtendList() {
		super("extendList", DataTypes.VOID);
		setBody(new Block(null));
		addParameter(new VariableDeclarationNode("listToBeExtended", DataTypes.LIST));
		addParameter(new VariableDeclarationNode("listToExtend", DataTypes.LIST));

	}

	@Override
	public Object call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
		setArguments(arguments, callFileName, callLine);
		var listToBeExtended = (List) getBody().getVariableByName("listToBeExtended").getValue();
		var listToExtend = (List) getBody().getVariableByName("listToExtend").getValue();

		return listToBeExtended.copy().append(listToExtend);
	}

}
