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

package sml.data.array;

import java.math.BigInteger;
import java.util.ArrayList;

import ast.Block;
import ast.declarations.FunctionDeclarationNode;
import ast.declarations.VariableDeclarationNode;
import ast.expressions.OperationNode;
import parser.DataTypes;

public class Subarray extends FunctionDeclarationNode {

	private static final long serialVersionUID = -92018-201966361416331207L;
	Array array;
	public Subarray(Array array) {
		super("subarray", DataTypes.ANY);
		this.array = array;
		setBody(new Block(array));
		addParameter(new VariableDeclarationNode("begin", DataTypes.INTEGER));
		addParameter(new VariableDeclarationNode("end", DataTypes.INTEGER));

	}


	@Override
	public Object call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
		setArguments(arguments, callFileName, callLine);
		var body = getBody();
		var begin = ((BigInteger) body.getVariableByName("begin").getValue()).intValue();
		var end = ((BigInteger) body.getVariableByName("end").getValue()).intValue();
		return array.subarray(begin, end);
	}

}
