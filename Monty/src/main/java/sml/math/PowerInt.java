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

package sml.math;

import java.math.BigInteger;
import java.util.ArrayList;

import ast.Block;
import ast.declarations.FunctionDeclarationNode;
import ast.declarations.VariableDeclarationNode;
import ast.expressions.OperationNode;
import parser.DataTypes;

public class PowerInt extends FunctionDeclarationNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = -978562413881839627L;

	public PowerInt() {
		super("powerInt", DataTypes.INTEGER);
		setBody(new Block(null));
		addParameter(new VariableDeclarationNode("base", DataTypes.INTEGER));
		addParameter(new VariableDeclarationNode("exponent", DataTypes.INTEGER));

	}

	@Override
	public Object call(ArrayList<OperationNode> arguments) {
		setArguments(arguments);
		var body = getBody();
		var base = (BigInteger) body.getVariableByName("base").getValue();
		var exponent = (BigInteger) body.getVariableByName("exponent").getValue();
		return base.pow(exponent.intValue());
	}

}
