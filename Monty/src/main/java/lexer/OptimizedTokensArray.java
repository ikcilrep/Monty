/*
Copyright 2018-2019 Szymon Perlicki

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

import java.util.Iterator;

public class OptimizedTokensArray implements Iterable<Token> {
	protected Token[] array;
	public int top;

	public OptimizedTokensArray() {
		clear();
	}

	public OptimizedTokensArray append(Token element) {
		top++;

		if (array.length == top)
			setLength(array.length << 1);
		array[top] = element;
		return this;
	}

	public void clear() {
		top = -1;
		array = new Token[64];
	}

	public Token get(int index) {
		return array[index];
	}

	@Override
	public Iterator<Token> iterator() {
		return new Iterator<Token>() {
			int counter = 0;

			@Override
			public boolean hasNext() {
				return counter <= top;

			}

			@Override
			public Token next() {
				return array[counter++];
			}
		};
	}

	public int length() {
		return top + 1;
	}

	public void setLength(int length) {
		var newArray = new Token[length];
		for (int i = 0; i < array.length && i < length; i++) {
			newArray[i] = array[i];
		}
		array = newArray;
	}

	public OptimizedTokensArray subarray(int begin, int end) {
		OptimizedTokensArray newArray = new OptimizedTokensArray();
		for (int i = begin; i < end; i++)
			newArray.append(get(i));
		return newArray;
	}

	public void trimToSize() {
		setLength(top + 1);
	}
}
