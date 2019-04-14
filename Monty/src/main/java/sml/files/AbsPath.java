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
import monty.Importing;
import parser.DataTypes;

public final class AbsPath extends FunctionDeclarationNode {

	public AbsPath() {
		super("absPath", DataTypes.STRING);
		setBody(new Block(null));
		addParameter("path", DataTypes.STRING);
	}

	public static String absPath(String path) {
		if (new File(path).isAbsolute())
			return path;
		return Importing.mainFileLocation + path;
	}

	@Override
	public String call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
		setArguments(arguments, callFileName, callLine);
		return absPath(getBody().getStringVariableValue("path"));
	}
}
