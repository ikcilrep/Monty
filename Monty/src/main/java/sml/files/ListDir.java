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
import parser.DataTypes;
import parser.LogError;
import sml.data.array.Array;

public final class ListDir extends FunctionDeclarationNode {

	public ListDir() {
		super("listDir", DataTypes.ANY);
		setBody(new Block(null));
		addParameter("path", DataTypes.STRING);
	}

	@Override
	public Array call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
		setArguments(arguments, callFileName, callLine);
		var path = (String) getBody().getVariable("path").getValue();
		try {
			var list = new File(AbsPath.absPath(path)).list();
			return list == null ? new Array(0) : new Array(list);
		} catch (SecurityException e) {
			new LogError("Access denied to:\t" + path, callFileName, callLine);
		}
		return null;
	}
}
