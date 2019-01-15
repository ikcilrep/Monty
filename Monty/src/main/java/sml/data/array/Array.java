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

package sml.data.array;

import java.util.Iterator;

import sml.data.list.List;
import sml.data.returning.Nothing;
import sml.data.stack.Stack;

public class Array<T> implements Iterable<T>, Cloneable {
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
	public Array(Array<T> array) {
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
	public Array<T> append(Array<T> elements) {
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
	public Array<T> append(T element) {
		top++;

		if (array.length == top)
			array = getNewArray(array.length << 1);
		array[top] = element;
		return this;
	}

	/*
	 * Description.
	 */
	public boolean contains(T toSearch) {
		for (int i = 0; i <= top; i++)
			if (array[i].equals(toSearch))
				return true;
		return false;

	}

	@SuppressWarnings("unchecked")
	public Array<T> copy() {
		try {
			return (Array<T>) clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * Description.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object other) {
		Array<Object> otherArray = null;
		if (other instanceof Array)
			otherArray = (Array<Object>) other;
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
	@SuppressWarnings("unchecked")
	public T get(int index) {
		return (T)array[index];
	}

	@Override
	public Iterator<T> iterator() {
		return new Iterator<T>() {
			int counter = 0;

			@Override
			public boolean hasNext() {
				return counter <= top;

			}

			@SuppressWarnings("unchecked")
			@Override
			public T next() {
				return (T)array[counter++];
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
	public Array<T> replaceAll(T toBeReplaced, T replacement) {
		for (int i = 0; i < length(); i++)
			if (array[i].equals(toBeReplaced))
				array[i] = replacement;
		return this;
	}

	/*
	 * Description.
	 */
	public Array<T> replaceFirst(T toBeReplaced, T replacement) {
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
	public Array<T> replaceLast(T toBeReplaced, T replacement) {
		for (int i = top; i < length(); i++)
			if (array[i].equals(toBeReplaced)) {
				array[i] = replacement;
				break;
			}
		return this;
	}

	public Array<T> reversed() {
		var arr = new Array<T>(length());
		for (int i = 0; i < length(); i++)
			arr.set(length() - i, (T) get(i));
		return arr;
	}

	/*
	 * Description.
	 */
	public Array<T> set(int index, T element) {
		array[index] = element;
		return this;
	}

	/*
	 * Description.
	 */
	public Array<T> subarray(int begin, int end) {
		Array<T> newArray = new Array<T>();
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
