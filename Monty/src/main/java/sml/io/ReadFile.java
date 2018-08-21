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

import java.io.File;
import java.util.ArrayList;

import ast.Block;
import ast.declarations.FunctionDeclarationNode;
import ast.declarations.VariableDeclarationNode;
import ast.expressions.OperationNode;
import monty.Main;
import parser.DataTypes;

public class ReadFile extends FunctionDeclarationNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = -111484253001907349L;

	public ReadFile() {
		super("readFile", DataTypes.STRING);
		setBody(new Block(null));
		addParameter(new VariableDeclarationNode("path", DataTypes.STRING));
	}

	@Override
	public Object call(ArrayList<OperationNode> arguments) {
		setArguments(arguments);
		var body = getBody();
		var path = (String) body.getVariableByName("path").getValue();
		var file = new File(path);
		if (file.isAbsolute())
			return monty.FileIO.readFile(file.getAbsolutePath());
		return monty.FileIO.readFile(new File(Main.path).getParent() + File.separatorChar + new File(path).getName());
	}

}
