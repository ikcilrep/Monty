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

package sml.data.returning;

import java.util.ArrayList;

import ast.Block;
import ast.declarations.FunctionDeclarationNode;
import ast.expressions.OperationNode;
import parser.DataTypes;

public class Nothing extends FunctionDeclarationNode {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1016665906573389526L;
	public static VoidType nothing = new VoidType();
	public static BreakType breakType = new BreakType();
	public static ContinueType continueType = new ContinueType();

	public Nothing() {
		super("nothing", DataTypes.VOID);
		new Block(null);
	}

	@Override
	public Object call(ArrayList<OperationNode> arguments) {
		setArguments(arguments);
		return nothing;
	}

}
