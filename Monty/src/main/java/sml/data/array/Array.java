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

import java.io.Serializable;
import java.util.Iterator;

import ast.Block;
import ast.declarations.StructDeclarationNode;
import sml.data.list.List;
import sml.data.returning.Nothing;
import sml.data.stack.Stack;

public class Array extends StructDeclarationNode implements Iterable<Object>, Cloneable, Serializable {
	private static final long serialVersionUID = -114997948039751189L;
	protected Object[] array;
	public void addFunctions() {
		addFunction(new AppendToArray(this));
		addFunction(new ExtendArray(this));
		addFunction(new GetFromArray(this));
		addFunction(new DoesArrayContains(this));
		addFunction(new LengthOfArray(this));
		addFunction(new ReplaceAllInArray(this));
		addFunction(new ReplaceFirstInArray(this));
		addFunction(new ReplaceLastInArray(this));
		addFunction(new SetInArray(this));
		addFunction(new Subarray(this));
	}
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
	
	public void setLength(int length) {
		int i = 0;
		var newArray = new Object[length];
		for (Object e : array)
			newArray[i++] = e;
		array = newArray;
	}
	
	public Array extend(Array elements) {
		var length = length();
		setLength(array.length+elements.length());
		var newLength = length();
		for(int i = length, j = 0; i < newLength; i++, j++)
			array[i] = elements.get(j);
		return this;
	}
	
	public Array append(Object element) {
		setLength(length()+1);
		array[length()-1] = element;
		return this;
	}

	public boolean contains(Object toSearch) {
		for (int i = 0; i < length(); i++)
			if (array[i].equals(toSearch))
				return true;
		return false;

	}

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
		int i = 0;
		for (Object e : otherArray)
			if (!array[i++].equals(e))
				return false;
		return true;

	}

	public Object get(int index) {
		return array[index];
	}

	@Override
	public Iterator<Object> iterator() {
		return new Iterator<Object>() {
			int counter = 0;

			@Override
			public boolean hasNext() {
				return counter <= length();

			}

			@Override
			public Object next() {
				return (Object)array[counter++];
			}
		};
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
		for (int i = length()-1; i >= 0; i--)
			if (array[i].equals(toBeReplaced)) {
				array[i] = replacement;
				break;
			}
		return this;
	}

	public Array reversed() {
		var arr = new Array(length());
		for (int i = 0, j = length()-1; i < length(); i++, j--)
			arr.set(j,  array[i]);
		return arr;
	}

	public Array set(int index, Object element) {
		array[index] = element;
		return this;
	}

	public Array subarray(int begin, int end) {
		Array newArray = new Array(end-begin+1);
		for (int i = begin,j =0; i < end; i++, j++)
			newArray.set(j, array[i]);
		return newArray;
	}

	public Object[] toArray() {
		return array;
	}

	public List toList() {
		var list = new List(array[0]);
		for (Object e : array)
			list.append(e);
		return list.getNext();

	}

	public Stack toStack() {
		return new Stack(array);
	}

	@Override
	public String toString() {
		var length = length();
		var stringBuilder = new StringBuilder(array.length + 2);
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
