/*
Copyright 2018-2019 Szymon Perlicki

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUObject WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package sml.data.array;

import ast.Block;
import ast.declarations.StructDeclarationNode;
import sml.data.returning.Nothing;
import sml.data.stack.Stack;

public final class Array extends StructDeclarationNode {

	protected Object[] array;

	public Array(int length) {
		super(new Block(null), "Array");
		addFunctions();
		array = new Object[length];
		for (int i = 0; i < length; i++)
			array[i] = Nothing.nothing;
	}

	public Array(Object[] array) {
		super(new Block(null), "Array");
		addFunctions();
		this.array = array;
	}

	public void addFunctions() {
		new Append(this);
		new Extend(this);
		new Get(this);
		new Length(this);
		new Replace(this);
		new ReplaceFirst(this);
		new ReplaceLast(this);
		new Set(this);
		new Subarray(this);
		new Find(this);
		new FindFirst(this);
		new FindLast(this);
		new NewIterator(this);
		new Reversed(this);
	}

	public Array append(Object element) {
		setLength(length() + 1);
		array[length() - 1] = element;
		return this;
	}

	public Array find(Object element) {
		Array result = new Array(0);
		for (int i = 0; i < length(); i++)
			if (array[i].equals(element))
				result.append(i);
		return result;
	}

	public int findFirst(Object element) {
		for (int i = 0; i < length(); i++)
			if (array[i].equals(element))
				return i;
		return -1;
	}

	public int findLast(Object element) {
		for (int i = length() - 1; i >= 0; i--)
			if (array[i].equals(element))
				return i;
		return -1;
	}

	public Object get(int index) {
		return array[index];
	}

	public int length() {
		return array.length;
	}

	public Array replace(Object toBeReplaced, Object replacement) {
		for (int i = 0; i < length(); i++)
			if (array[i].equals(toBeReplaced))
				array[i] = replacement;
		return this;
	}

	public Array replaceFirst(Object toBeReplaced, Object replacement) {
		for (int i = 0; i < length(); i++)
			if (array[i].equals(toBeReplaced)) {
				array[i] = replacement;
				break;
			}
		return this;
	}

	public Array replaceLast(Object toBeReplaced, Object replacement) {
		for (int i = length() - 1; i >= 0; i--)
			if (array[i].equals(toBeReplaced)) {
				array[i] = replacement;
				break;
			}
		return this;
	}

	public Array reversed() {
		var arr = new Array(length());
		for (int i = 0, j = length() - 1; i < length(); i++, j--)
			arr.set(j, array[i]);
		return arr;
	}

	public Array set(int index, Object element) {
		array[index] = element;
		return this;
	}

	public Array setLength(int length) {
		var newArray = new Object[length];
		var i = 0;
		for (; i < length && i < length(); i++)
			newArray[i] = array[i];
		for (; i < length; i++)
			newArray[i] = Nothing.nothing;
		array = newArray;
		return this;
	}

	public Array extend(Array elements) {
		var length = length();
		setLength(length + elements.length());
		var newLength = length();
		for (int i = length, j = 0; i < newLength; i++, j++)
			set(i, elements.get(j));
		return this;
	}

	public boolean contains(Object toSearch) {
		for (int i = 0; i < length(); i++)
			if (get(i).equals(toSearch))
				return true;
		return false;
	}

	@Override
	public String toString() {
		var length = length();
		var stringBuilder = new StringBuilder(length * 2 + 1);
		stringBuilder.append('[');
		int i = 0;
		while (true) {
			stringBuilder.append(get(i).toString());
			if (++i < length)
				stringBuilder.append(',');
			else
				break;
		}
		stringBuilder.append(']');
		return stringBuilder.toString();

	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof Array))
			return false;
		var length = length();
		var otherArray = (Array) other;
		if (otherArray.length() != length)
			return false;
		for (int i = 0; i < length; i++)
			if (!get(i).equals(otherArray.get(i)))
				return false;
		return true;

	}

	public Array subarray(int begin, int end) {
		Array newArray = new Array(end - begin);
		for (int i = begin, j = 0; i < end; i++, j++)
			newArray.set(j, array[i]);
		return newArray;
	}

	public Object[] toArray() {
		return array;
	}

	public Stack toStack() {
		return new Stack(array);
	}

}
