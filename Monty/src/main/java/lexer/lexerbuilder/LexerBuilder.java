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

package lexer.lexerbuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.text.StringEscapeUtils;

import lexer.Token;

public class LexerBuilder extends SetOnSomething implements Cloneable {

	final ArrayList<Character> binaryOperators = new ArrayList<>();

	private boolean canIAddQuotes = false;

	final ArrayList<Character> comparisonOperators = new ArrayList<>();

	private Token copied;

	final Pattern floatLiteralIdentifier = Pattern.compile("[+-]?[0-9]+\\.[0-9]+");

	final Pattern integerLiteralIdentifier = Pattern.compile("[+-]?[0-9]+");

	final StringBuilder keyword = new StringBuilder();

	final private HashMap<String, Token> keywords = new HashMap<>();

	final ArrayList<Character> logicalOperators = new ArrayList<Character>();

	private Token matchedValue;

	final Pattern regexIdentifier = Pattern.compile("^[a-zA-Z_$][a-zA-Z_$0-9]*$");

	final private HashMap<Pattern, Token> regularExpressions = new HashMap<>();

	private String text;
	private int tokenCounter = -1;
	private int lines;

	final ArrayList<Token> tokens = new ArrayList<Token>();

	public LexerBuilder(String text, String fileName) {
		super.fileName = fileName;
		lines = 1;
		binaryOperators.add('*');
		binaryOperators.add('/');
		binaryOperators.add('+');
		binaryOperators.add('-');
		binaryOperators.add('%');
		binaryOperators.add('<');
		binaryOperators.add('>');
		binaryOperators.add('&');
		binaryOperators.add('|');
		binaryOperators.add('^');
		binaryOperators.add('~');
		comparisonOperators.add('!');
		comparisonOperators.add('=');
		comparisonOperators.add('<');
		comparisonOperators.add('>');
		logicalOperators.add('&');
		logicalOperators.add('|');
		logicalOperators.add('!');
		setText(text);
	}

	public void addQuotesToStringText(boolean canIAddQuotes) {
		this.canIAddQuotes = canIAddQuotes;
	}

	private void check() throws CloneNotSupportedException {
		String keywordToString = keyword.toString().substring(0, keyword.length() - 1);
		if (keywords.containsKey(keywordToString)) {
			copied = keywords.get(keywordToString).copy();
			copied.setText(keywordToString);
			copied.setLine(lines);
			tokens.add(copied);
		} else if ((matchedValue = matched(keywordToString)) != null) {
			copied = matchedValue.copy();
			copied.setText(keywordToString);
			copied.setLine(lines);
			tokens.add(copied);
		} else if (tokenFloat != null && isFloatLiteral(keywordToString)) {
			copied = tokenFloat.copy();
			copied.setText(keywordToString);
			copied.setLine(lines);
			tokens.add(copied);
		} else if (tokenInteger != null && isIntegerLiteral(keywordToString)) {
			copied = tokenInteger.copy();
			copied.setText(keywordToString);
			copied.setLine(lines);
			tokens.add(copied);
		} else if (tokenIdentifier != null && isIdentifier(keywordToString)) {
			copied = tokenIdentifier.copy();
			copied.setText(keywordToString);
			copied.setLine(lines);
			tokens.add(copied);
		}
	}

	public ArrayList<Token> getAllTokens() {
		tokens.clear();
		keyword.setLength(0);
		boolean isInString = false;
		boolean isInComment = false;
		boolean writeNextEscapeChar = false;
		int i = 0;
		while (i < text.length()) {
			char thisChar = text.charAt(i);
			if (isInComment) {
				if (thisChar == endCommentChar)
					isInComment = false;
				i++;
				continue;
			}
			if (isInString) {
				if (writeNextEscapeChar) {
					keyword.append(thisChar);
					writeNextEscapeChar = false;
				} else if (thisChar == '\\') {
					keyword.append(thisChar);
					writeNextEscapeChar = true;
				} else if (thisChar == '\"') {
					try {
						copied = tokenString.copy();
					} catch (CloneNotSupportedException e) {
						e.printStackTrace();
					}
					copied.setText(StringEscapeUtils.unescapeJava(keyword.toString()));
					copied.setLine(lines);
					if (canIAddQuotes)
						copied.setText("\"" + copied.getText() + "\"");
					tokens.add(copied);
					isInString = false;
					keyword.setLength(0);
				} else
					keyword.append(thisChar);
				i++;
				continue;
			}
			keyword.append(thisChar);
			if (thisChar != '\0' && thisChar == commentChar) {
				if (thisChar == '\n')
					lines++;
				isInComment = true;
			} else if (tokenString != null && thisChar == '\"') {
				isInString = true;
				keyword.setLength(0);
			} else if (Character.isWhitespace(thisChar)) {
				if (thisChar == '\n')
					lines++;
				try {
					check();
				} catch (CloneNotSupportedException e) {
					e.printStackTrace();
				}
				keyword.setLength(0);
			} else if (tokenComma != null && thisChar == ',') {
				try {
					check();
				} catch (CloneNotSupportedException e) {
					e.printStackTrace();
				}
				tokens.add(tokenComma);
				keyword.setLength(0);
			} else if (tokenDot != null && thisChar == '.') {
				if (i > 0) {
					if (!Character.isDigit(text.charAt(i - 1))) {
						try {
							check();
						} catch (CloneNotSupportedException e) {
							e.printStackTrace();
						}
						tokens.add(tokenDot);
						keyword.setLength(0);
					}
				}

			} else if (tokenSemicolon != null && thisChar == ';') {
				try {
					check();
				} catch (CloneNotSupportedException e) {
					e.printStackTrace();
				}
				tokens.add(tokenSemicolon);
				keyword.setLength(0);
			} else if (tokenColon != null && thisChar == ';') {
				try {
					check();
				} catch (CloneNotSupportedException e) {
					e.printStackTrace();
				}
				tokens.add(tokenColon);
				keyword.setLength(0);
			} else if (tokenBracket != null && (thisChar == '(' || thisChar == ')')) {
				try {
					check();
				} catch (CloneNotSupportedException e) {
					e.printStackTrace();
				}
				try {
					copied = tokenBracket.copy();
				} catch (CloneNotSupportedException e) {
					e.printStackTrace();
				}
				copied.setText("" + thisChar);
				copied.setLine(lines);
				tokens.add(copied);
				keyword.setLength(0);
			} else if (tokenCurlyBracket != null && (thisChar == '{' || thisChar == '}')) {
				try {
					check();
				} catch (CloneNotSupportedException e) {
					e.printStackTrace();
				}
				try {
					copied = tokenCurlyBracket.copy();
				} catch (CloneNotSupportedException e) {
					e.printStackTrace();
				}
				copied.setText("" + thisChar);
				copied.setLine(lines);
				tokens.add(copied);
				keyword.setLength(0);
			} else if (tokenSquareBracket != null && (thisChar == '[' || thisChar == ']')) {
				try {
					check();
				} catch (CloneNotSupportedException e) {
					e.printStackTrace();
				}
				try {
					copied = tokenSquareBracket.copy();
				} catch (CloneNotSupportedException e) {
					e.printStackTrace();
				}
				copied.setText("" + thisChar);
				copied.setLine(lines);
				tokens.add(copied);
				keyword.setLength(0);
			} else if (tokenComparisonOperator != null && comparisonOperators.contains(thisChar)
					&& ((i <= text.length() - 2) && (text.charAt(i + 1) == '=' || (thisChar == '<' || thisChar == '>')
							&& (text.charAt(i + 1) != '<' && text.charAt(i + 1) != '>')))) {
				try {
					check();
				} catch (CloneNotSupportedException e) {
					e.printStackTrace();
				}
				try {
					copied = tokenComparisonOperator.copy();
				} catch (CloneNotSupportedException e) {
					e.printStackTrace();
				}
				if ((thisChar == '<' || thisChar == '>') && text.charAt(i + 1) != '=') {
					copied.setText("" + thisChar);
					copied.setLine(lines);
				} else {
					copied.setText("" + thisChar + '=');
					copied.setLine(lines);
					i++;
				}
				tokens.add(copied);
				keyword.setLength(0);

			} else if (tokenLogicalOperator != null && logicalOperators.contains(thisChar)) {
				if ((thisChar == '&' || thisChar == '|')
						&& (i <= text.length() - 2 && text.charAt(i + 1) == thisChar)) {
					try {
						check();
					} catch (CloneNotSupportedException e) {
						e.printStackTrace();
					}
					try {
						copied = tokenLogicalOperator.copy();
					} catch (CloneNotSupportedException e) {
						e.printStackTrace();
					}
					copied.setText(text.substring(i, i + 2));
					copied.setLine(lines);
					i++;
				} else if (thisChar == '!'
						&& (i == text.length() - 1 || (i <= text.length() - 2) && text.charAt(i + 1) != '=')) {
					try {
						copied = tokenLogicalOperator.copy();
					} catch (CloneNotSupportedException e) {
						e.printStackTrace();
					}
					copied.setText("!");
					copied.setLine(lines);
				}
				tokens.add(copied);
				keyword.setLength(0);

			} else if (((i + 1 < text.length() && !Character.isDigit(text.charAt(i + 1)) || i + 1 >= text.length()))
					&& (tokenBinaryOperator != null || tokenAssignmentOperator != null)
					&& binaryOperators.contains(thisChar) || thisChar == '=') {
				try {
					check();
				} catch (CloneNotSupportedException e) {
					e.printStackTrace();
				}
				boolean isAssignmentOperator = false;
				if (tokenAssignmentOperator != null) {
					if (i <= text.length() - 3
							&& (text.substring(i, i + 3).equals("<<=") || text.substring(i, i + 3).equals(">>="))) {
						try {
							copied = tokenAssignmentOperator.copy();
						} catch (CloneNotSupportedException e) {
							e.printStackTrace();
						}
						copied.setText(text.substring(i, i + 3));
						copied.setLine(lines);
						i += 2;
						isAssignmentOperator = true;
					} else if (i <= text.length() - 2 && binaryOperators.contains(thisChar)
							&& text.charAt(i + 1) == '=') {
						try {
							copied = tokenAssignmentOperator.copy();
						} catch (CloneNotSupportedException e) {
							e.printStackTrace();
						}
						copied.setText(text.substring(i, i + 2));
						copied.setLine(lines);
						i++;
						isAssignmentOperator = true;

					} else if (thisChar == '=') {
						try {
							copied = tokenAssignmentOperator.copy();
						} catch (CloneNotSupportedException e) {
							e.printStackTrace();
						}
						copied.setText("" + thisChar);
						copied.setLine(lines);
						isAssignmentOperator = true;

					}
				}
				if (!isAssignmentOperator && tokenBinaryOperator != null) {
					if (i <= text.length() - 2 && text.substring(i, i + 2).equals("<<")
							|| text.substring(i, i + 2).equals(">>")) {
						try {
							copied = tokenBinaryOperator.copy();
						} catch (CloneNotSupportedException e) {
							e.printStackTrace();
						}
						copied.setText(text.substring(i, i + 2));
						copied.setLine(lines);
						i++;
					} else if (binaryOperators.contains(thisChar)) {
						try {
							copied = tokenBinaryOperator.copy();
						} catch (CloneNotSupportedException e) {
							e.printStackTrace();
						}
						copied.setText("" + thisChar);
						copied.setLine(lines);
					}
				}
				keyword.setLength(0);
				tokens.add(copied);
			}
			i++;
		}
		return tokens;

	}

	public Token getCurrentToken() {
		if (tokenCounter >= 0)
			return tokens.get(tokenCounter);
		return null;
	}

	public String getIdentifierRegex() {
		return "^[a-zA-Z_$][a-zA-Z_$0-9]*$";
	}

	/*
	 * If you want to change options between the tokens.
	 */
	public Token getNextToken() {
		getAllTokens();
		if (tokens != null && tokenCounter + 1 < tokens.size())
			return tokens.get(++tokenCounter);
		return null;
	}

	private boolean isFloatLiteral(String str) {
		return floatLiteralIdentifier.matcher(str).matches();
	}

	/*
	 * isIdentifier(String str) returns true if str can be identifier
	 */
	private boolean isIdentifier(String str) {
		return regexIdentifier.matcher(str).matches();
	}

	private boolean isIntegerLiteral(String str) {
		return integerLiteralIdentifier.matcher(str).matches();
	}

	private Token matched(String str) {
		for (Entry<Pattern, Token> entry : regularExpressions.entrySet()) {
			Matcher keyMatcher = entry.getKey().matcher(str);
			Token value = entry.getValue();
			if (keyMatcher.matches())
				return value;
		}
		return null;
	}

	public void removeKeyword(String keyword) {
		if (keywords.containsKey(keyword))
			keywords.remove(keyword);
		else
			System.out.println("Keyword " + keyword + " wasn't set.");
	}

	public void removeRegex(String regex) {
		Pattern compiledRegex = Pattern.compile(regex);
		if (regularExpressions.containsKey(compiledRegex))
			regularExpressions.remove(compiledRegex);
		else
			System.out.println("Regex " + regex + " wasn't set.");
	}

	public void setKeyword(String keyword, Token token) {
		token.setFileName(fileName);
		keywords.put(keyword, token);
	}

	public void setRegex(String regex, Token token) {
		token.setFileName(fileName);
		regularExpressions.put(Pattern.compile(regex), token);
	}

	public void setText(String text) {
		this.text = " " + text + " ";
	}

	public LexerBuilder copy() {
		try {
			return (LexerBuilder) clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}
}
