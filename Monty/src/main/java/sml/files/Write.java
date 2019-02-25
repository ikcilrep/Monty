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
package sml.files;

import java.io.File;
import java.util.ArrayList;

import ast.Block;
import ast.declarations.FunctionDeclarationNode;
import ast.expressions.OperationNode;
import monty.FileIO;
import monty.Importing;
import parser.DataTypes;
import sml.data.returning.Nothing;
import sml.data.returning.VoidType;

public class Write extends FunctionDeclarationNode {

	public Write() {
		super("write", DataTypes.VOID);
		setBody(new Block(null));
		addParameter("path", DataTypes.STRING);
		addParameter("text", DataTypes.STRING);
		addParameter("isAppend", DataTypes.BOOLEAN);
	}

	@Override
	public VoidType call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
		setArguments(arguments, callFileName, callLine);
		var body = getBody();
		var path = getBody().getVariable("path").getValue().toString();
		var text = getBody().getVariable("text").getValue().toString();
		var isAppend = (boolean) body.getVariable("isAppend").getValue();
		var file = new File(path);
		if (file.isAbsolute())
			FileIO.writeFile(path, text, isAppend);
		else
			FileIO.writeFile(Importing.mainFileLocation + path, text, isAppend);

		return Nothing.nothing;
	}

}
