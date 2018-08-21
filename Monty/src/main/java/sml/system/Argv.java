package sml.system;
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

import java.math.BigInteger;
import java.util.ArrayList;

import ast.Block;
import ast.declarations.FunctionDeclarationNode;
import ast.declarations.VariableDeclarationNode;
import ast.expressions.OperationNode;
import monty.Main;
import parser.DataTypes;
import parser.MontyException;

public class Argv extends FunctionDeclarationNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3955613798131108880L;

	public Argv() {
		super("argv", DataTypes.STRING);
		setBody(new Block(null));
		addParameter(new VariableDeclarationNode("index", DataTypes.INTEGER));
	}

	@Override
	public Object call(ArrayList<OperationNode> arguments) {
		setArguments(arguments);
		var index = ((BigInteger) getBody().getVariableByName("index").getValue()).intValue();
		if (index >= Main.argv.length)
			new MontyException("Index " + index + " is too large for length " + Main.argv.length);
		return Main.argv[index];
	}

}
