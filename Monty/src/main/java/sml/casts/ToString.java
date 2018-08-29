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

package sml.casts;

import java.util.ArrayList;

import ast.Block;
import ast.declarations.FunctionDeclarationNode;
import ast.declarations.VariableDeclarationNode;
import ast.expressions.OperationNode;
import parser.DataTypes;
import parser.MontyException;

public class ToString extends FunctionDeclarationNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2725148737303045967L;
	public ToString() {
		super("toString", DataTypes.STRING);
		setBody(new Block(null));
		addParameter(new VariableDeclarationNode("a", DataTypes.ANY));
	}
	public static String toString(Object a) {
		if (a == null)
			new MontyException("Can't cast void to string.");
		return a.toString();
	}
	@Override
	public Object call(ArrayList<OperationNode> arguments) {
		setArguments(arguments);
		var a = getBody().getVariableByName("a").getValue();
		return toString(a);
	}

}
