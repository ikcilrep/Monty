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

package parser.parsing;

import ast.Block;
import ast.declarations.CustomFunctionDeclarationNode;
import ast.declarations.StructDeclarationNode;
import ast.declarations.VariableDeclarationNode;
import ast.expressions.ConstantNode;
import ast.expressions.FunctionCallNode;
import ast.expressions.OperationNode;
import ast.expressions.VariableNode;
import ast.statements.BreakStatementNode;
import ast.statements.ChangeToStatementNode;
import ast.statements.ContinueStatementNode;
import ast.statements.ForStatementNode;
import ast.statements.IfStatementNode;
import ast.statements.ReturnStatementNode;
import ast.statements.WhileStatementNode;
import lexer.OptimizedTokensArray;
import lexer.TokenTypes;
import parser.DataTypes;
import parser.LogError;
import parser.Tokens;

public abstract class AdderToBlock {
	public final static void addBreakStatement(Block block, String fileName, int line) {
		block.addChild(new BreakStatementNode(fileName, line));
	}

	public final static void addChangeToStatement(Block block, OptimizedTokensArray tokens) {
		block.addChild(new ChangeToStatementNode(new VariableNode(tokens.get(1).getText()),
				Tokens.getDataType(tokens.get(3).getType()), tokens.get(0).getFileName(), tokens.get(0).getLine(),
				block));
	}

	public final static void addContinueStatement(Block block, String fileName, int line) {
		block.addChild(new ContinueStatementNode(fileName, line));
	}

	public final static Block addDoWhileStatement(Block block, OptimizedTokensArray tokens) {
		var whileStatement = new WhileStatementNode(ExpressionParser.parse(block, tokens.subarray(2, tokens.length())),
				tokens.get(0).getFileName(), tokens.get(0).getLine(), true, block);
		block.addChild(whileStatement);
		return whileStatement;

	}

	public final static Block addElseStatement(Block block, OptimizedTokensArray tokens) {
		if (!(block instanceof IfStatementNode))
			new LogError("Unexpected \"else\" keyword", tokens.get(0));
		var elseBlock = new Block(block.getParent());
		elseBlock.setFileName(tokens.get(0).getFileName());
		elseBlock.setLine(tokens.get(0).getLine());
		((IfStatementNode) block).setElseBody(elseBlock);

		return elseBlock;
	}

	public final static void addExpression(Block block, OptimizedTokensArray tokensBeforeSemicolon) {
		block.addChild(ExpressionParser.parse(block, tokensBeforeSemicolon));
	}

	public final static Block addForStatement(Block block, OptimizedTokensArray tokens) {
		var variableName = tokens.get(1).getText();
		if (Character.isUpperCase(variableName.charAt(0)))
			new LogError("Variable name " + variableName + " should start with lower case", tokens.get(1));
		var forStatement = new ForStatementNode(variableName,
				ExpressionParser.parse(block, tokens.subarray(3, tokens.length())), tokens.get(0).getFileName(),
				tokens.get(0).getLine(), block);
		block.addChild(forStatement);
		return forStatement;
	}

	public final static Block addFunctionDeclaration(Block block, OptimizedTokensArray tokens) {
		var functionName = tokens.get(2).getText();
		var function = new CustomFunctionDeclarationNode(functionName, Tokens.getDataType(tokens.get(1).getType()));
		if (Character.isUpperCase(functionName.charAt(0)))
			new LogError("Function name " + functionName + " should start with lower case", tokens.get(2));
		DataTypes type = null;
		String name = null;
		for (int i = 3; i < tokens.length(); i++) {
			var tokenType = tokens.get(i).getType();
			var isTokenTypeEqualsComma = tokens.get(i).getType().equals(TokenTypes.COMMA);
			if (tokenType.equals(TokenTypes.IDENTIFIER)) {
				name = tokens.get(i).getText();
				if (Character.isUpperCase(name.charAt(0)))
					new LogError("Argument name " + name + " should start with lower case", tokens.get(i));
			} else if (!isTokenTypeEqualsComma)
				type = Tokens.getDataType(tokenType);
			if (isTokenTypeEqualsComma || i + 1 >= tokens.length())
				function.addParameter(name, type);
		}
		function.setBody(new Block(block));
		block.addFunction(function, tokens.get(1));
		return function.getBody();
	}

	public final static Block addIfStatement(Block block, OptimizedTokensArray tokens) {
		var ifStatement = new IfStatementNode(block, ExpressionParser.parse(block, tokens.subarray(1, tokens.length())),
				tokens.get(0).getFileName(), tokens.get(0).getLine());
		block.addChild(ifStatement);
		return ifStatement;

	}

	public final static void addReturnStatement(Block block, OptimizedTokensArray tokens) {
		OperationNode expression = null;
		if (tokens.length() > 1)
			expression = ExpressionParser.parse(block, tokens.subarray(1, tokens.length()));
		else
			expression = new OperationNode(new FunctionCallNode("nothing"), block);
		block.addChild(new ReturnStatementNode(expression, tokens.get(0).getFileName(), tokens.get(0).getLine()));
	}

	public final static Block addStructDeclaration(Block block, OptimizedTokensArray tokens) {
		var name = tokens.get(1).getText();
		if (Character.isLowerCase(name.charAt(0)))
			new LogError("Struct name should start with upper case", tokens.get(0));
		var struct = new StructDeclarationNode(block, name);
		struct.addNewStruct(block, tokens.get(0));
		return struct;
	}

	public final static void addVariableDeclaration(Block block, OptimizedTokensArray tokens) {
		var isDynamic = tokens.get(0).getType().equals(TokenTypes.DYNAMIC_KEYWORD);
		int n = isDynamic ? 3 : 2;
		var dataType = Tokens.getDataType(tokens.get(n - 2).getType());
		var name = tokens.get(n - 1).getText();
		if (Character.isUpperCase(name.charAt(0)))
			new LogError("Variable name " + name + " should start with lower case", tokens.get(1));
		var variable = new VariableDeclarationNode(name, dataType);
		variable.setDynamic(isDynamic);
		block.addVariable(variable, tokens.get(n - 2));
		if (tokens.length() > n)
			addExpression(block, tokens.subarray(n - 1, tokens.length()));
		else {
			var operation = new OperationNode("=", block);
			operation.setLeftOperand(new OperationNode(new VariableNode(name), block));
			operation.setRightOperand(
					new OperationNode(new ConstantNode(DataTypes.getNeutralValue(dataType), dataType), block));
			block.addChild(operation);
		}

	}	

	public final static Block addWhileStatement(Block block, OptimizedTokensArray tokens) {
		var whileStatement = new WhileStatementNode(ExpressionParser.parse(block, tokens.subarray(1, tokens.length())),
				tokens.get(0).getFileName(), tokens.get(0).getLine(), false, block);
		block.addChild(whileStatement);
		return whileStatement;

	}
}
