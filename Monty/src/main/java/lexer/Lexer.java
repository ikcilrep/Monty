package lexer;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.text.StringEscapeUtils;

import parser.LogError;

public class Lexer {
	private static List<Character> operatorsParts = List.of('+', '-', '*', '/', '!', '<', '>', '=', '|', '&', '%', '^');

	private static LinkedList<Token> floatLiteral(String code, String integer, String fileName, int line,
			LinkedList<Token> tokens, int i) {
		var tokenText = integer;
		while (++i < code.length() && Character.isDigit(code.charAt(i)))
			tokenText += code.charAt(i);
		tokens.add(new Token(TokenTypes.FLOAT_LITERAL, tokenText, fileName, line));
		return lex(code, fileName, line, tokens, i);
	}

	private static LinkedList<Token> identifierOrKeyword(String code, String fileName, int line,
			LinkedList<Token> tokens, int i) {
		var tokenText = "" + code.charAt(i);
		while (++i < code.length() && Character.isJavaIdentifierPart(code.charAt(i)))
			tokenText += code.charAt(i);
		tokens.add(new Token(keywordOrIdentifierToTokenType(tokenText), tokenText, fileName, line));
		return lex(code, fileName, line, tokens, i);
	}

	private static TokenTypes interpunctionToTokenType(char tokenText) {
		switch (tokenText) {
		case '.':
			return TokenTypes.DOT;
		case ',':
			return TokenTypes.COMMA;
		case '(':
		case ')':
			return TokenTypes.BRACKET;
		default:
			return TokenTypes.SEMICOLON;
		}

	}

	private static TokenTypes keywordOrIdentifierToTokenType(String tokenText) {
		switch (tokenText) {
		case "if":
			return TokenTypes.IF_KEYWORD;
		case "else":
			return TokenTypes.ELSE_KEYWORD;
		case "end":
			return TokenTypes.END_KEYWORD;
		case "func":
			return TokenTypes.FUNC_KEYWORD;
		case "return":
			return TokenTypes.RETURN_KEYWORD;
		case "int":
			return TokenTypes.INTEGER_KEYWORD;
		case "void":
			return TokenTypes.VOID_KEYWORD;
		case "float":
			return TokenTypes.FLOAT_KEYWORD;
		case "string":
			return TokenTypes.STRING_KEYWORD;
		case "boolean":
			return TokenTypes.BOOLEAN_KEYWORD;
		case "array":
			return TokenTypes.ARRAY_KEYWORD;
		case "list":
			return TokenTypes.LIST_KEYWORD;
		case "stack":
			return TokenTypes.STACK_KEYWORD;
		case "any":
			return TokenTypes.ANY_KEYWORD;
		case "for":
			return TokenTypes.FOR_KEYWORD;
		case "while":
			return TokenTypes.WHILE_KEYWORD;
		case "in":
			return TokenTypes.IN_KEYWORD;
		case "break":
			return TokenTypes.BREAK_KEYWORD;
		case "continue":
			return TokenTypes.CONTINUE_KEYWORD;
		case "import":
			return TokenTypes.IMPORT_KEYWORD;
		case "jar":
			return TokenTypes.JAR_KEYWORD;
		case "dynamic":
			return TokenTypes.DYNAMIC_KEYWORD;
		case "static":
			return TokenTypes.STATIC_KEYWORD;
		case "change":
			return TokenTypes.CHANGE_KEYWORD;
		case "do":
			return TokenTypes.DO_KEYWORD;
		case "to":
			return TokenTypes.TO_KEYWORD;
		case "thread":
			return TokenTypes.THREAD_KEYWORD;
		case "true":
		case "false":
			return TokenTypes.BOOLEAN_LITERAL;
		default:
			return TokenTypes.IDENTIFIER;
		}
	}

	public static LinkedList<Token> lex(String code, String fileName, int line, LinkedList<Token> tokens, int i) {
		var isInComment = false;
		for (; i < code.length(); i++) {
			char c = code.charAt(i);
			if (c == '\n') {
				line++;
				isInComment = false;
			}
			if (!isInComment) {
				if (c == '#')
					isInComment = true;
				if (c == ';' || c == ',' || c == '.' || c == '(' || c == ')')
					tokens.add(new Token(interpunctionToTokenType(c), "" + c, fileName, line));
				if (c == '\"')
					return stringLiteral(code, fileName, line, tokens, i);
				if (Character.isDigit(c)
						|| ((c == '+' || c == '-') && (i + 1 < code.length() && Character.isDigit(code.charAt(i + 1)))))
					return number(code, fileName, line, tokens, i);
				if (operatorsParts.contains((Character) c))
					return operator(code, fileName, line, tokens, i);
				if (Character.isJavaIdentifierStart(c))
					return identifierOrKeyword(code, fileName, line, tokens, i);
			}
		}
		return tokens;
	}

	private static LinkedList<Token> number(String code, String fileName, int line, LinkedList<Token> tokens, int i) {
		var tokenText = "" + code.charAt(i);
		while (++i < code.length() && Character.isDigit(code.charAt(i)))
			tokenText += code.charAt(i);
		if (code.charAt(i) == '.')
			return floatLiteral(code, tokenText + '.', fileName, line, tokens, i);
		tokens.add(new Token(TokenTypes.INTEGER_LITERAL, tokenText, fileName, line));
		return lex(code, fileName, line, tokens, i);
	}

	private static LinkedList<Token> operator(String code, String fileName, int line, LinkedList<Token> tokens, int i) {
		var tokenText = "" + code.charAt(i);
		while (++i < code.length() && operatorsParts.contains((Character) code.charAt(i)))
			tokenText += code.charAt(i);
		Token token = new Token(operatorToTokenType(tokenText, fileName, line), tokenText, fileName, line);
		tokens.add(token);
		return lex(code, fileName, line, tokens, i);
	}

	private static TokenTypes operatorToTokenType(String tokenText, String fileName, int line) {
		switch (tokenText) {
		case "!":
		case "+":
		case "-":
		case "*":
		case "/":
		case "<":
		case ">":
		case "&":
		case "|":
		case "^":
		case "=":
		case "<<":
		case ">>":
		case "!=":
		case "+=":
		case "-=":
		case "*=":
		case "/=":
		case "<=":
		case ">=":
		case "&=":
		case "|=":
		case "^=":
		case "==":
		case "<<=":
		case ">>=":
			return TokenTypes.OPERATOR;
		default:
			new LogError("Unknown operator:\t" + tokenText, fileName, line);
		}
		return null;
	}

	private static LinkedList<Token> stringLiteral(String code, String fileName, int line, LinkedList<Token> tokens,
			int i) {
		var tokenText = "";
		try {
			while (code.charAt(++i - 1) == '\\' || code.charAt(i) != '\"')
				tokenText += code.charAt(i);
		} catch (IndexOutOfBoundsException e) {
			new LogError("String wasn't closed", fileName, line);
		}
		Token token = new Token(TokenTypes.STRING_LITERAL, StringEscapeUtils.unescapeJava(tokenText), fileName, line);
		tokens.add(token);
		return lex(code, fileName, line, tokens, i + 1);
	}
}
