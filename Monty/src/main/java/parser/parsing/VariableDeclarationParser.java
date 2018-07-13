package parser.parsing;

import java.util.List;

import ast.declarations.VariableDeclarationNode;
import lexer.MontyToken;
import parser.DataTypes;

public class VariableDeclarationParser {
	public static VariableDeclarationNode parse(List<MontyToken> declaration) {
		var dataType = (DataTypes) null;
		switch (declaration.get(0).getType()) {
		case INTEGER_KEYWORD:
			dataType = DataTypes.INTEGER;
			break;
		case FLOAT_KEYWORD:
			dataType = DataTypes.FLOAT;
			break;
		case BOOLEAN_KEYWORD:
			dataType = DataTypes.BOOLEAN;
			break;
		case STRING_KEYWORD:
			dataType = DataTypes.STRING;
			break;
		default:
			break;
		}
		return new VariableDeclarationNode(declaration.get(1).getText(), dataType);
	}
}
