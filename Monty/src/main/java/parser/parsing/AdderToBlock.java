package parser.parsing;

import java.util.List;

import ast.Block;
import ast.declarations.FunctionDeclarationNode;
import ast.declarations.VariableDeclarationNode;
import ast.expressions.ExpressionNode;
import ast.statements.IfStatementNode;
import ast.statements.PrintStatementNode;
import ast.statements.ReturnStatementNode;
import lexer.MontyToken;
import lexer.TokenTypes;
import parser.DataTypes;
import parser.Tokens;

public abstract class AdderToBlock {

	public static void addExpression(Block block, List<MontyToken> tokens) {
		block.addChild(ExpressionParser.parse(tokens));
	}

	public static void addPrintStatement(Block block, List<MontyToken> tokens) {
		block.addChild(new PrintStatementNode(ExpressionParser.parse(tokens.subList(1, tokens.size()))));
	}

	public static void addReturnStatement(Block block, List<MontyToken> tokens) {
		var expression = (ExpressionNode) null;
		if (tokens.size() > 1)
			expression = ExpressionParser.parse(tokens.subList(1, tokens.size()));
		block.addChild(new ReturnStatementNode(expression));
	}

	public static void addVariableDeclaration(Block block, List<MontyToken> tokens) {
		block.addVariable(
				new VariableDeclarationNode(tokens.get(2).getText(), Tokens.getDataType(tokens.get(1).getType())));
		addExpression(block, tokens.subList(2, tokens.size()));
	}

	public static Block addFunctionDeclaration(Block block, List<MontyToken> tokens) {
		var function = new FunctionDeclarationNode(tokens.get(2).getText(),
				Tokens.getDataType(tokens.get(1).getType()));
		var type = (DataTypes) null;
		var name = (String) null;
		for (int i = 3; i < tokens.size(); i++) {
			if (tokens.get(i).getType().equals(TokenTypes.IDENTIFIER))
				name = tokens.get(i).getText();
			else if (tokens.get(i).getType().equals(TokenTypes.INTEGER_KEYWORD)
					|| tokens.get(i).getType().equals(TokenTypes.FLOAT_KEYWORD)
					|| tokens.get(i).getType().equals(TokenTypes.STRING_KEYWORD)
					|| tokens.get(i).getType().equals(TokenTypes.BOOLEAN_KEYWORD))
				type = Tokens.getDataType(tokens.get(i).getType());
			if (tokens.get(i).getType().equals(TokenTypes.COMMA) || i + 1 >= tokens.size())
				function.addParameter(new VariableDeclarationNode(name, type));
		}
		function.setBody(new Block(block));
		block.addChild(function);
		return function.getBody();
	}

	public static Block addIfStatement(Block block, List<MontyToken> tokens) {
		var ifStatement =new IfStatementNode(ExpressionParser.parse(tokens.subList(1, tokens.size()))); 
		ifStatement.setThenBody(new Block(block));	
		block.addChild(ifStatement);
		return ifStatement.getThenBody();

	}
}
