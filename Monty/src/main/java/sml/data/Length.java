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

package sml.data;

import java.math.BigInteger;

import ast.Block;
import ast.declarations.CustomFunctionDeclarationNode;
import ast.declarations.VariableDeclarationNode;
import ast.expressions.ConstantNode;
import ast.expressions.OperationNode;
import ast.expressions.VariableNode;
import ast.statements.ForStatementNode;
import ast.statements.ReturnStatementNode;
import parser.DataTypes;

public class Length extends CustomFunctionDeclarationNode {

	public Length() {
		super("length", DataTypes.INTEGER);
		var body = new Block(null);
		setBody(body);
		var counter = new VariableDeclarationNode("counter", DataTypes.INTEGER);
		counter.setValue(DataTypes.getNeutralValue(counter.getType()));
		body.addVariable(counter, fileName, line);
		addParameter(new VariableDeclarationNode("iterableObject", DataTypes.ANY));
		var forStatement = new ForStatementNode("_", new OperationNode(new VariableNode("iterableObject"), body),
				fileName, line, body);
		var incrementor = new OperationNode("+=", forStatement);
		incrementor.setLeftOperand(new OperationNode(new VariableNode("counter"), forStatement));
		incrementor
				.setRightOperand(new OperationNode(new ConstantNode(BigInteger.ONE, DataTypes.INTEGER), forStatement));
		forStatement.addChild(incrementor);
		body.addChild(forStatement);
		body.addChild(new ReturnStatementNode(new OperationNode(new VariableNode("counter"), body), fileName, line));
	}

}
