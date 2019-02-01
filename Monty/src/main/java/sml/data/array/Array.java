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

public class Array extends StructDeclarationNode implements Cloneable {

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
		addFunction(new Append(this));
		addFunction(new Extend(this));
		addFunction(new Get(this));
		addFunction(new Length(this));
		addFunction(new Replace(this));
		addFunction(new ReplaceFirst(this));
		addFunction(new ReplaceLast(this));
		addFunction(new Set(this));
		addFunction(new Subarray(this));
		addFunction(new Find(this));
		addFunction(new FindFirst(this));
		addFunction(new FindLast(this));
		addFunction(new NewIterator(this));
		addFunction(new Reversed(this));
	}

	public Array append(Object element) {
		setLength(length() + 1);
		array[length() - 1] = element;
		return this;
	}

	public boolean contains(Object toSearch) {
		for (int i = 0; i < length(); i++)
			if (array[i].equals(toSearch))
				return true;
		return false;

	}

	@Override
	public Array copy() {
		try {
			return (Array) clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean equals(Object other) {
		Array otherArray = null;
		if (other instanceof Array)
			otherArray = (Array) other;
		else
			return false;
		if (length() != otherArray.length())
			return false;
		for (int i = 0; i < otherArray.length(); i++)
			if (!array[i].equals(otherArray.get(i)))
				return false;
		return true;

	}

	public Array extend(Array elements) {
		var length = length();
		setLength(array.length + elements.length());
		var newLength = length();
		for (int i = length, j = 0; i < newLength; i++, j++)
			array[i] = elements.get(j);
		return this;
	}

	public Array findAll(Object element) {
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

	public Array replaceAll(Object toBeReplaced, Object replacement) {
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

	public void setLength(int length) {
		int i = 0;
		var newArray = new Object[length];
		for (Object e : array)
			newArray[i++] = e;
		array = newArray;
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

	@Override
	public String toString() {
		var length = length();
		var stringBuilder = new StringBuilder(array.length * 2 + 1);
		stringBuilder.append('[');
		int i = 0;
		while (true) {
			stringBuilder.append(array[i].toString());
			if (++i < length)
				stringBuilder.append(',');
			else
				break;
		}
		stringBuilder.append(']');
		return stringBuilder.toString();

	}
}
