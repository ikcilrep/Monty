package parser;

import java.util.List;

import ast.Block;
import ast.declarations.VariableDeclarationNode;
import ast.expressions.ExpressionNode;
import ast.statements.PrintStatementNode;
import ast.statements.ReturnStatementNode;
import lexer.MontyToken;
import parser.parsing.ExpressionParser;

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
		block.addChild(
				new VariableDeclarationNode(tokens.get(2).getText(), Tokens.getDataType(tokens.get(1).getType())));
	}
}
