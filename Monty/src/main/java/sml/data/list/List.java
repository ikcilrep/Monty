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

package sml.data.list;

import java.util.Iterator;

import parser.LogError;
import sml.data.array.Array;
import sml.data.returning.Nothing;

public class List implements Iterable<Object>, Cloneable {
	private List next = null;
	List last = this;
	Object value = Nothing.nothing;

	public List() {

	}

	public List(Object value) {
		this.value = value;
	}

	public List append(List element) {
		last.setNext(element);
		last = last.getNext();
		while (last.getNext() != null)
			last = last.getNext();
		return this;
	}

	public List append(Object element) {
		last.setNext(new List(element));
		last = last.getNext();
		return this;
	}

	public boolean contains(Object element) {
		return value == element ? true : (getNext() != null ? getNext().contains(element) : false);
	}

	public List copy() {
		try {
			return (List) clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean equals(Object other) {
		List otherList = null;
		if (other instanceof List)
			otherList = (List) other;
		else
			return false;
		if (value.equals(otherList.value)) {
			if (next == null && otherList.next == null)
				return true;
			return next.equals(otherList.next);
		}
		return false;

	}

	public Object get(int index) {
		return index == 0 ? value : getNext().get(index - 1);
	}

	public List getNext() {
		return next;
	}

	@Override
	public Iterator<Object> iterator() {
		return new Iterator<Object>() {
			List list = copy();

			@Override
			public boolean hasNext() {
				return list != null;

			}

			@Override
			public Object next() {
				var value = list.value;
				list = list.getNext();
				return value;
			}
		};
	}

	public int length() {
		return next != null ? 1 + next.length() : 1;
	}

	public List replaceAll(Object toBeReplaced, Object replacement) {
		value = value == toBeReplaced ? replacement : value;
		if (getNext() != null)
			getNext().replaceAll(toBeReplaced, replacement);
		return this;
	}

	public List replaceFirst(Object toBeReplaced, Object replacement) {
		if (value == toBeReplaced)
			value = replacement;
		else if (getNext() != null)
			getNext().replaceFirst(toBeReplaced, replacement);
		return this;
	}

	public List replaceLast(Object toBeReplaced, Object replacement) {
		return reversed().replaceFirst(toBeReplaced, replacement).reversed();
	}

	public List reversed() {
		return getNext() == null ? new List(value) : getNext().reversed().append(value);
	}

	public List set(int index, Object element) {
		if (index == 0)
			value = element;
		else if (getNext() != null)
			return getNext().set(index - 1, element);
		else
			new LogError("Index went away from range");
		return this;
	}

	public void setNext(List next) {
		this.next = next;
	}

	public List sublist(int begin) {
		if (begin == 0)
			return this;
		else if (getNext() == null)
			new LogError("Index went away from range");
		return getNext().sublist(begin - 1);
	}

	public List sublist(int begin, int end) {
		return sublist(begin).reversed().sublist(length() - end).reversed();
	}

	public Array<Object> toArray() {
		Array<Object> array = new Array<Object>(length());
		var list = this;
		int i = 0;
		while (i < array.length()) {
			array.set(i++, list.value);
			list = list.getNext();
		}
		return array;
	}

	@Override
	public String toString() {
		var stringBuilder = new StringBuilder((length() << 1) + 1);
		stringBuilder.append('[');
		int i = 0;
		for (Object e : this) {
			stringBuilder.append(e.toString());
			if (++i < length())
				stringBuilder.append(',');
		}
		stringBuilder.append(']');
		return stringBuilder.toString();

	}
}
