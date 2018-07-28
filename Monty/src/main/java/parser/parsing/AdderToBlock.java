package parser.parsing;
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
import java.util.List;

import ast.Block;
import ast.NodeTypes;
import ast.declarations.CustomFunctionDeclarationNode;
import ast.declarations.VariableDeclarationNode;
import ast.expressions.OperationNode;
import ast.expressions.VariableNode;
import ast.statements.ChangeToStatementNode;
import ast.statements.IfStatementNode;
import ast.statements.ReturnStatementNode;
import ast.statements.WhileStatementNode;
import lexer.MontyToken;
import lexer.TokenTypes;
import parser.DataTypes;
import parser.MontyException;
import parser.Tokens;

public abstract class AdderToBlock {

	public static void addExpression(Block block, List<MontyToken> tokens) {
		block.addChild(ExpressionParser.parse(block, tokens));
	}

	public static void addReturnStatement(Block block, List<MontyToken> tokens) {
		var expression = (OperationNode) null;
		if (tokens.size() > 1)
			expression = ExpressionParser.parse(block, tokens.subList(1, tokens.size()));
		block.addChild(new ReturnStatementNode(expression));
	}

	public static void addVariableDeclaration(Block block, List<MontyToken> tokens) {
		var variable = new VariableDeclarationNode(tokens.get(3).getText(),
				Tokens.getDataType(tokens.get(2).getType()));
		variable.setDynamic(true);
		block.addVariable(variable);
		addExpression(block, tokens.subList(3, tokens.size()));
	}

	public static Block addFunctionDeclaration(Block block, List<MontyToken> tokens) {
		var function = new CustomFunctionDeclarationNode(tokens.get(2).getText(),
				Tokens.getDataType(tokens.get(1).getType()));
		var type = (DataTypes) null;
		var name = (String) null;
		for (int i = 3; i < tokens.size(); i++) {
			var tokenType = tokens.get(i).getType();
			var isTokenTypeEqualsComma = tokens.get(i).getType().equals(TokenTypes.COMMA);
			if (tokenType.equals(TokenTypes.IDENTIFIER))
				name = tokens.get(i).getText();
			else if (!isTokenTypeEqualsComma)
				type = Tokens.getDataType(tokenType);
			if (isTokenTypeEqualsComma || i + 1 >= tokens.size())
				function.addParameter(new VariableDeclarationNode(name, type));
		}
		function.setBody(new Block(block));
		block.addFunction(function);
		return function.getBody();
	}

	public static Block addIfStatement(Block block, List<MontyToken> tokens) {
		var ifStatement = new IfStatementNode(block, ExpressionParser.parse(block, tokens.subList(1, tokens.size())));
		block.addChild(ifStatement);
		return ifStatement;

	}

	public static Block addElseStatement(Block block, List<MontyToken> tokens) {
		if (!block.getNodeType().equals(NodeTypes.IF_STATEMENT))
			new MontyException("Unexpected \"else\" keyword.");
		var elseBlock = new Block(block.getParent());
		((IfStatementNode) block).setElseBody(elseBlock);
		return elseBlock;
	}

	public static Block addWhileStatement(Block block, List<MontyToken> tokens) {
		var whileStatement = new WhileStatementNode(ExpressionParser.parse(block, tokens.subList(1, tokens.size())));
		whileStatement.setBody(new Block(block));
		block.addChild(whileStatement);
		return whileStatement.getBody();

	}

	public static void addChangeToStatement(Block block, List<MontyToken> tokens) {
		block.addChild(new ChangeToStatementNode(new VariableNode(tokens.get(1).getText()),
				Tokens.getDataType(tokens.get(3).getType())));
	}

}
