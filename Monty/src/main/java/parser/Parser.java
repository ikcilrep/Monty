package parser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import ast.Node;
import lexer.MontyToken;
import lexer.TokenTypes;

public class Parser {
	// Strings used in regular expressions more than one times:
	private static final String DATA_TYPES = "INTEGER_LITERAL|BOOLEAN_LITERAL|STRING_LITERAL|FLOAT_LITERAL";
	private static final String DATA_TYPES_KEYWORDS = "INTEGER_KEYWORD|BOOLEAN_KEYWORD|STRING_KEYWORD|FLOAT_KEYWORD";
	private static final String EXPRESSION = "(((IDENTIFIER|" + DATA_TYPES + ") )+(OPERATOR )*)+";

	// Regular Expressions:

	private static final Pattern VARIABLE_DECLARATION_REGEX = Pattern
			.compile("^VAR_KEYWORD (" + DATA_TYPES_KEYWORDS + ") " + EXPRESSION + "$");
	private static final Pattern EXPRESSION_REGEX = Pattern.compile("^" + EXPRESSION + "$");
	private static final Pattern PRINT_STATEMENT_REGEX = Pattern.compile("^PRINT_KEYWORD " + EXPRESSION + "$");

	// Methods:

	public static String getText(List<MontyToken> tokens) {
		var text = new StringBuilder();
		for (MontyToken token : tokens)
			text.append(token.getType() + " ");
		return text.toString();
	}

	public static Node parse(List<MontyToken> tokens) {
		var tokensBeforeSemicolon = new ArrayList<MontyToken>();
		for (MontyToken token : tokens) {
			if (token.getType().equals(TokenTypes.SEMICOLON)) {
				String text = getText(tokensBeforeSemicolon);
				System.out.println("\n" + text);
				if (EXPRESSION_REGEX.matcher(text).matches())
					System.out.println("EXPRESSION!");
				else if (VARIABLE_DECLARATION_REGEX.matcher(text).matches())
					System.out.println("VARIABLE DECLARATION!");
				else if (PRINT_STATEMENT_REGEX.matcher(text).matches())
					System.out.println("PRINT STATEMENT!");
				tokensBeforeSemicolon.clear();
			} else
				tokensBeforeSemicolon.add(token);
		}
		return null;

	}
}
