package sml.io;
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

import java.util.ArrayList;

import ast.Block;
import ast.declarations.FunctionDeclarationNode;
import ast.declarations.VariableDeclarationNode;
import ast.expressions.OperationNode;
import parser.DataTypes;
import sml.data.returning.Nothing;

public class Print extends FunctionDeclarationNode{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5657836899374280597L;

	public Print() {
		super("print", DataTypes.VOID);
		setBody(new Block(null));
		addParameter(new VariableDeclarationNode("toPrint", DataTypes.ANY));
	}

	@Override
	public Object call(ArrayList<OperationNode> arguments) {
		setArguments(arguments);
		System.out.print(getBody().getVariableByName("toPrint").getValue());
		return Nothing.nothing;
	}

}
