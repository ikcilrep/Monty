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

public abstract class SetOnSomething<T extends Token<T>> {
	protected T tokenIdentifier;

	protected T tokenFloat;

	protected T tokenInteger;

	protected T tokenString;

	protected T tokenBinaryOperator;

	protected T tokenAssignmentOperator;

	protected T tokenComparisonOperator;

	protected T tokenLogicalOperator;

	protected T tokenComma;

	protected T tokenDot;

	protected T tokenSemicolon;

	protected T tokenColon;

	protected T tokenBracket;

	protected T tokenCurlyBracket;

	protected T tokenSquareBracket;

	protected char commentChar = '\0';
	protected char endCommentChar = '\0';

	public void setOnIdentifier(T tokenIdentifier) {
		this.tokenIdentifier = tokenIdentifier;
	}

	public void setOnFloatLiteral(T tokenFloat) {
		this.tokenFloat = tokenFloat;
	}

	public void setOnIntegerLiteral(T tokenInteger) {
		this.tokenInteger = tokenInteger;
	}

	public void setOnStringLiteral(T tokenString) {
		this.tokenString = tokenString;
	}

	public void setOnBinaryOperator(T tokenBinaryOperator) {
		this.tokenBinaryOperator = tokenBinaryOperator;
	}

	public void setOnAssignmentOperator(T tokenAssignmentOperator) {
		this.tokenAssignmentOperator = tokenAssignmentOperator;
	}

	public void setOnComparisonOperator(T tokenComparisonOperator) {
		this.tokenComparisonOperator = tokenComparisonOperator;
	}

	public void setOnLogicalOperator(T tokenLogicalOperator) {
		this.tokenLogicalOperator = tokenLogicalOperator;
	}

	public void setOnComma(T tokenComma) {
		this.tokenComma = tokenComma;
		this.tokenComma.setText(",");
	}

	public void setOnDot(T tokenDot) {
		this.tokenDot = tokenDot;
		this.tokenDot.setText(".");
	}

	public void setOnSemicolon(T tokenSemicolon) {
		this.tokenSemicolon = tokenSemicolon;
		this.tokenSemicolon.setText(";");
	}

	public void setOnColon(T tokenColon) {
		this.tokenColon = tokenColon;
		this.tokenColon.setText(":");
	}

	public void setOnBracket(T tokenBracket) {
		this.tokenBracket = tokenBracket;
		this.tokenBracket.setText(":");
	}

	public void setOnCurlyBracket(T tokenCurlyBracket) {
		this.tokenCurlyBracket = tokenCurlyBracket;
		this.tokenCurlyBracket.setText(":");
	}

	public void setOnSquareBracket(T tokenSquareBracket) {
		this.tokenSquareBracket = tokenSquareBracket;
		this.tokenSquareBracket.setText(":");
	}

	public void setCommentChar(char commentChar, char endCommentChar) {
		this.commentChar = commentChar;
		this.endCommentChar = endCommentChar;
	}

}
