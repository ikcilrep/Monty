package sml.data.list;

import parser.MontyException;
import sml.data.array.Array;

public class LinkedList {
	LinkedList next = null;
	Object value;

	public LinkedList(Object value) {
		this.value = value;
	}

	public LinkedList append(Object element) {
		if (next == null)
			next = new LinkedList(element);
		else
			next.append(element);
		return this;
	}

	public LinkedList append(LinkedList element) {
		if (next == null)
			next = element;
		else
			next.append(element);
		return this;
	}

	public boolean contains(Object element) {
		return value == element ? true : (next != null ? next.contains(element) : false);
	}

	public int length() {
		return next != null ? 1 + next.length() : 1;
	}

	public Array toArray() {
		Array array = new Array(length());
		var list = this;
		int i = 0;
		while (i < array.length()) {
			array.set(i++, list.value);
			list = list.next;
		}
		return array;
	}

	public Object get(int index) {
		return index == 0 ? value
				: (next != null ? next.get(index - 1) : new MontyException("Index went away from range"));
	}

	public LinkedList set(int index, Object element) {
		if (index == 0)
			value = element;
		else if (next != null)
			return next.set(index - 1, element);
		else
			new MontyException("Index went away from range");
		return this;
	}

	public LinkedList replaceAll(Object toBeReplaced, Object replacement) {
		value = value == toBeReplaced ? replacement : value;
		if (next != null)
			next.replaceAll(toBeReplaced, replacement);
		return this;
	}

	public LinkedList replaceFirst(Object toBeReplaced, Object replacement) {
		if (value == toBeReplaced)
			value = replacement;
		else if (next != null)
			next.replaceFirst(toBeReplaced, replacement);
		return this;
	}

	public LinkedList reversed() {
		return next == null ? new LinkedList(value) : next.reversed().append(value);
	}

	public LinkedList reverse() {
		var rd = reversed();
		value = rd.value;
		next = rd.next;
		return rd;
	}

	public LinkedList replaceLast(Object toBeReplaced, Object replacement) {
		return reverse().replaceFirst(toBeReplaced, replacement).reverse();
	}
	
	public LinkedList subList(int begin) {
		if (begin == 0)
			return this;
		else if (next == null)
			new MontyException("Index went away from range");
		return next.subList(begin-1);
	}
}
