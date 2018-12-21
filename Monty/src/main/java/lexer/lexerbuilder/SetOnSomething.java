/*
Copyright 2018 Szymon Perlicki

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUToken WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package lexer.lexerbuilder;

import lexer.Token;

public abstract class SetOnSomething {
	protected String fileName;

	protected Token tokenIdentifier;

	protected Token tokenFloat;

	protected Token tokenInteger;

	protected Token tokenString;

	protected Token tokenBinaryOperator;

	protected Token tokenAssignmentOperator;

	protected Token tokenComparisonOperator;

	protected Token tokenLogicalOperator;

	protected Token tokenComma;

	protected Token tokenDot;

	protected Token tokenSemicolon;

	protected Token tokenColon;

	protected Token tokenBracket;

	protected Token tokenCurlyBracket;

	protected Token tokenSquareBracket;

	protected char commentChar = '\0';
	protected char endCommentChar = '\0';

	public void setOnIdentifier(Token tokenIdentifier) {
		this.tokenIdentifier = tokenIdentifier;
		this.tokenIdentifier.setFileName(fileName);
	}

	public void setOnFloatLiteral(Token tokenFloat) {
		this.tokenFloat = tokenFloat;
		this.tokenFloat.setFileName(fileName);

	}

	public void setOnIntegerLiteral(Token tokenInteger) {
		this.tokenInteger = tokenInteger;
		this.tokenInteger.setFileName(fileName);

	}

	public void setOnStringLiteral(Token tokenString) {
		this.tokenString = tokenString;
		this.tokenString.setFileName(fileName);

	}

	public void setOnBinaryOperator(Token tokenBinaryOperator) {
		this.tokenBinaryOperator = tokenBinaryOperator;
		this.tokenBinaryOperator.setFileName(fileName);
	}

	public void setOnAssignmentOperator(Token tokenAssignmentOperator) {
		this.tokenAssignmentOperator = tokenAssignmentOperator;
		this.tokenAssignmentOperator.setFileName(fileName);
	}

	public void setOnComparisonOperator(Token tokenComparisonOperator) {
		this.tokenComparisonOperator = tokenComparisonOperator;
		this.tokenComparisonOperator.setFileName(fileName);
	}

	public void setOnLogicalOperator(Token tokenLogicalOperator) {
		this.tokenLogicalOperator = tokenLogicalOperator;
		this.tokenLogicalOperator.setFileName(fileName);
	}

	public void setOnComma(Token tokenComma) {
		this.tokenComma = tokenComma;
		this.tokenComma.setText(",");
		this.tokenComma.setFileName(fileName);
	}

	public void setOnDot(Token tokenDot) {
		this.tokenDot = tokenDot;
		this.tokenDot.setText(".");
		this.tokenDot.setFileName(fileName);
	}

	public void setOnSemicolon(Token tokenSemicolon) {
		this.tokenSemicolon = tokenSemicolon;
		this.tokenSemicolon.setText(";");
		this.tokenSemicolon.setFileName(fileName);
	}

	public void setOnColon(Token tokenColon) {
		this.tokenColon = tokenColon;
		this.tokenColon.setText(":");
		this.tokenColon.setFileName(fileName);
	}

	public void setOnBracket(Token tokenBracket) {
		this.tokenBracket = tokenBracket;
		this.tokenBracket.setText(":");
		this.tokenBracket.setFileName(fileName);
	}

	public void setOnCurlyBracket(Token tokenCurlyBracket) {
		this.tokenCurlyBracket = tokenCurlyBracket;
		this.tokenCurlyBracket.setText(":");
		this.tokenCurlyBracket.setFileName(fileName);
	}

	public void setOnSquareBracket(Token tokenSquareBracket) {
		this.tokenSquareBracket = tokenSquareBracket;
		this.tokenSquareBracket.setText(":");
		this.tokenSquareBracket.setFileName(fileName);
	}

	public void setCommentChar(char commentChar, char endCommentChar) {
		this.commentChar = commentChar;
		this.endCommentChar = endCommentChar;
	}

}
