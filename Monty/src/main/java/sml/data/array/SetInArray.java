package sml.data.array;
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
import parser.DataTypes;

public class SetInArray extends FunctionDeclarationNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4263197015723594960L;

	public SetInArray() {
		super("setInArray", DataTypes.ARRAY);
		setBody(new Block(null));
		addParameter(new VariableDeclarationNode("arr", DataTypes.ARRAY));
		addParameter(new VariableDeclarationNode("index", DataTypes.INTEGER));
		addParameter(new VariableDeclarationNode("element", DataTypes.ANY));
	}

	@Override
	public Object call(ArrayList<OperationNode> arguments) {
		setArguments(arguments);
		var body = getBody();
		var arr = (Array) body.getVariableByName("arr").getValue();
		var index = ((BigInteger) body.getVariableByName("index").getValue()).intValue();
		var element = body.getVariableByName("element").getValue();
		return arr.set(index, element);
	}

}
