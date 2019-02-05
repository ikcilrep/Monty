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

package sml.system;

import java.math.BigInteger;
import java.util.ArrayList;

import ast.Block;
import ast.declarations.FunctionDeclarationNode;
import ast.expressions.OperationNode;
import monty.Main;
import parser.DataTypes;
import parser.LogError;

public final class Argv extends FunctionDeclarationNode {

	public Argv() {
		super("argv", DataTypes.STRING);
		setBody(new Block(null));
		addParameter("index", DataTypes.INTEGER);
	}

	@Override
	public Object call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
		setArguments(arguments, callFileName, callLine);
		var index = ((BigInteger) getBody().getVariable("index").getValue()).intValue() + 1;
		if (index >= Main.argv.length)
			new LogError("Index " + index + " is too large for length " + Main.argv.length, callFileName, callLine);
		return Main.argv[index];
	}

}
