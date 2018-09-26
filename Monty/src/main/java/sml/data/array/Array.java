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

package sml.data.array;

import java.util.Iterator;

import parser.MontyException;
import sml.data.list.LinkedList;

public class Array implements Iterable<Object>, Cloneable {
	protected Object[] array;

	/*
	 * Description.
	 */
	public Array() {
		array = new Object[0];
	}

	/*
	 * Description.
	 */
	public Array(int length) {
		array = new Object[length];
		for (int i = 0; i < length; i++)
			array[i] = null;
	}

	/*
	 * Description.
	 */
	public Array(Array array) {
		this.array = array.toArray();
	}

	/*
	 * Description.
	 */
	public Array(Object array[]) {
		this.array = new Object[array.length];
		int i = 0;
		for (Object e : array)
			this.array[i++] = e;
	}

	/*
	 * Description.
	 */
	public boolean isEmpty() {
		for (Object e : array)
			if (e != null)
				return true;
		return false;
	}

	/*
	 * Description.
	 */
	public boolean contains(Object toSearch) {
		for (Object e : array)
			if (e.equals(toSearch))
				return true;
		return false;

	}

	/*
	 * Description.
	 */
	public Array subArray(int begin, int end) {
		int length = end - begin + 1;
		Array newArray = new Array(length);
		for (int i = 0; i < length; i++)
			newArray.set(i, array[i]);

		return newArray;
	}

	/*
	 * Description.
	 */
	public Array append(Object element) {
		var newArray = new Object[array.length + 1];
		newArray[array.length] = element;
		int i = 0;
		for (Object e : array)
			newArray[i++] = e;
		array = newArray;
		return this;
	}

	/*
	 * Description.
	 */
	public Array append(Array elements) {
		var newArray = new Object[array.length + elements.length()];
		int i = 0;
		for (Object e : array)
			newArray[i++] = e;

		for (Object e : elements)
			newArray[i++] = e;
		array = newArray;

		return this;
	}

	/*
	 * Description.
	 */
	public Object[] toArray() {
		return array;
	}

	/*
	 * Description.
	 */
	public Object get(int index) {
		if (index >= array.length)
			new MontyException("Index " + index + " is too large for length " + array.length);
		return array[index];
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
	public int length() {
		return array.length;
	}

	/*
	 * Description.
	 */
	public Array replaceAll(Object toBeReplaced, Object replacement) {
		for (int i = 0; i < array.length; i++)
			if (array[i].equals(toBeReplaced))
				array[i] = replacement;
		return this;
	}

	/*
	 * Description.
	 */
	public Array replaceFirst(Object toBeReplaced, Object replacement) {
		for (int i = 0; i < array.length; i++)
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
		for (int i = array.length - 1; i < array.length; i++)
			if (array[i].equals(toBeReplaced)) {
				array[i] = replacement;
				break;
			}
		return this;
	}

	/*
	 * Description.
	 */
	public boolean equals(Array array) {
		if (array.length() != this.array.length)
			return false;
		int i = 0;
		for (Object e : array)
			if (!this.array[i++].equals(e))
				return false;
		return true;

	}

	public Array copy() {
		try {
			return (Array) clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String toString() {
		var stringBuilder = new StringBuilder((array.length<<1)+1);
		stringBuilder.append('[');
		int i = 0;
		for (Object e : array) {
			stringBuilder.append(e.toString());
			if (++i < array.length)
				stringBuilder.append(',');
		}
		stringBuilder.append(']');
		return stringBuilder.toString();

	}

	public LinkedList toList() {
		var list = new LinkedList(array[0]);
		for (Object e : array)
			list.append(e);
		return list.getNext();
		
	}
	
	@Override
	public Iterator<Object> iterator() {
		return new Iterator<Object>() {
			int counter = 0;
			
			@Override
			public boolean hasNext() {
				return counter < array.length;

			}

			@Override
			public Object next() {
				return array[counter++];
			}
		};
	}
}
