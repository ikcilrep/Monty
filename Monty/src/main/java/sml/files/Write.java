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

import java.util.ArrayList;

import ast.Block;
import ast.declarations.FunctionDeclarationNode;
import ast.expressions.OperationNode;
import monty.FileIO;
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
		FileIO.writeFile(AbsPath.absPath((String) getBody().getVariable("path").getValue()),
				(String) body.getVariable("text").getValue(), (boolean) body.getVariable("isAppend").getValue(),
				callFileName, callLine);
		return Nothing.nothing;
	}

}
