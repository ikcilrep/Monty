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

package lexer;

import lexer.lexerbuilder.Token;

public class MontyToken implements Token<MontyToken>, Cloneable{
	private String text;
	TokenTypes type;
	public MontyToken(TokenTypes type) {
		this.type = type;
	}
	public TokenTypes getType() {
		return type;
	}
	@Override
	public void setText(String text) {
		this.text = text;
	}

	@Override
	public String getText() {
		return text;
	}

	@Override
	public MontyToken copy() throws CloneNotSupportedException {
		return (MontyToken) super.clone();
	}

}
