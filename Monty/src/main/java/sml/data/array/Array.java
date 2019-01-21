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

import sml.data.list.List;
import sml.data.returning.Nothing;
import sml.data.stack.Stack;

public class Array implements Iterable<Object>, Cloneable, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -114997948039751189L;
	protected Object[] array;
	public int top;

	/*
	 * Description.
	 */
	public Array() {
		clear();
	}

	/*
	 * Description.
	 */
	public Array(Array array) {
		this.array = array.toArray();
		top = array.top;
	}

	/*
	 * Description.
	 */
	public Array(int length) {
		array = new Object[length];
		for (int i = 0; i < length; i++)
			array[i] = Nothing.nothing;
	}

	/*
	 * Description.
	 */
	public Array(Object array[]) {
		this.array = array;
	}

	public Object[] getNewArray(int length) {
		int i = 0;
		var newArray = new Object[length];
		for (Object e : array)
			newArray[i++] = e;
		return newArray;
	}
	
	/*
	 * Description.
	 */
	public Array append(Array elements) {
		var length = array.length+elements.length();
		top += elements.length();
		if (length >= top)
			array = getNewArray(length << 1);
		for (int i = 0; i < elements.length(); i++)
			array[top+i+1] = elements.get(i);
		return this;
	}
	
	/*
	 * Description.
	 */
	public Array append(Object element) {
		top++;

		if (array.length == top)
			array = getNewArray(array.length << 1);
		array[top] = element;
		return this;
	}

	/*
	 * Description.
	 */
	public boolean contains(Object toSearch) {
		for (int i = 0; i <= top; i++)
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

	/*
	 * Description.
	 */
	@Override
	public boolean equals(Object other) {
		Array otherArray = null;
		if (other instanceof Array)
			otherArray = (Array) other;
		else
			return false;
		if (top != otherArray.top)
			return false;
		int i = 0;
		for (Object e : otherArray)
			if (!array[i++].equals(e))
				return false;
		return true;

	}

	/*
	 * Description.
	 */
	public Object get(int index) {
		return array[index];
	}

	@Override
	public Iterator<Object> iterator() {
		return new Iterator<Object>() {
			int counter = 0;

			@Override
			public boolean hasNext() {
				return counter <= top;

			}

			@Override
			public Object next() {
				return (Object)array[counter++];
			}
		};
	}

	/*
	 * Description.
	 */
	public int length() {
		return top+1;
	}

	/*
	 * Description.
	 */
	public Array replaceAll(Object toBeReplaced, Object replacement) {
		for (int i = 0; i < length(); i++)
			if (array[i].equals(toBeReplaced))
				array[i] = replacement;
		return this;
	}

	/*
	 * Description.
	 */
	public Array replaceFirst(Object toBeReplaced, Object replacement) {
		for (int i = 0; i < length(); i++)
			if (array[i].equals(toBeReplaced)) {
				array[i] = replacement;
				break;
			}
		return this;
	}

	/*
	 * Description.
	 */
	public Array replaceLast(Object toBeReplaced, Object replacement) {
		for (int i = top; i < length(); i++)
			if (array[i].equals(toBeReplaced)) {
				array[i] = replacement;
				break;
			}
		return this;
	}

	public Array reversed() {
		var arr = new Array(length());
		for (int i = 0; i < length(); i++)
			arr.set(length() - i,  get(i));
		return arr;
	}

	/*
	 * Description.
	 */
	public Array set(int index, Object element) {
		array[index] = element;
		return this;
	}

	/*
	 * Description.
	 */
	public Array subarray(int begin, int end) {
		Array newArray = new Array();
		for (int i = begin; i < end; i++)
			newArray.append(get(i));

		return newArray;
	}

	/*
	 * Description.
	 */
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
	
	public void clear() {
		top = -1;
		array = new Object[64];
	}

	@Override
	public String toString() {
		var stringBuilder = new StringBuilder(array.length + 2);
		stringBuilder.append('[');
		int i = 0;
		while (true) {
			stringBuilder.append(array[i].toString());
			if (++i <= top)
				stringBuilder.append(',');
			else
				break;
		}
		stringBuilder.append(']');
		return stringBuilder.toString();

	}
}
