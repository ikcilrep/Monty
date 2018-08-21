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
import sml.data.returning.Nothing;

public class WriteFile extends FunctionDeclarationNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3412784350867342486L;

	public WriteFile() {
		super("writeFile", DataTypes.VOID);
		setBody(new Block(null));
		addParameter(new VariableDeclarationNode("path", DataTypes.STRING));
		addParameter(new VariableDeclarationNode("text", DataTypes.STRING));
		addParameter(new VariableDeclarationNode("isAppend", DataTypes.BOOLEAN));

	}

	@Override
	public Object call(ArrayList<OperationNode> arguments) {
		setArguments(arguments);
		var body = getBody();
		var path = (String) body.getVariableByName("path").getValue();
		var text = (String) body.getVariableByName("text").getValue();
		var isAppend = (Boolean) body.getVariableByName("isAppend").getValue();

		var file = new File(path);
		if (file.isAbsolute())
			monty.FileIO.writeFile(file.getAbsolutePath(), text, isAppend);
		else
			monty.FileIO.writeFile(new File(Main.path).getParent() + File.separatorChar + new File(path).getName(),
					text, isAppend);

		return Nothing.nothing;
	}

}
