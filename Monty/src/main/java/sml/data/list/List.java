package sml.data.list;

import ast.Block;
import ast.declarations.StructDeclarationNode;
import sml.data.returning.Nothing;

public class List extends StructDeclarationNode {
	Object head = Empty.empty;
	List tail = null;

	public List() {
		super(new Block(null), "List");
		addFunction(new Append(this));
		addFunction(new Contains(this));
		addFunction(new Extend(this));
		addFunction(new Find(this));
		addFunction(new FindFirst(this));
		addFunction(new FindLast(this));
		addFunction(new Get(this));
		addFunction(new Length(this));
		addFunction(new Replace(this));
		addFunction(new ReplaceFirst(this));
		addFunction(new ReplaceLast(this));
		addFunction(new Reversed(this));
		addFunction(new Set(this));
		addFunction(new Sublist(this));
		addFunction(new NewIterator(this));
	}

	public boolean contains(Object element) {
		return findFirst(element) != -1;
	}

	public List reversed() {
		var list = this;
		var length = length();
		var result = new List().setLength(length);
		for (int j = length - 1; j >= 0; j--) {
			result.set(j, list.head);
			list = list.tail;
		}
		return result;
	}

	public List setLength(int length) {
		var list = this;
		var _length = length();
		int i = 0;
		for (; i < length && i < _length; i++)
			list = list.tail;
		list.head = Empty.empty;
		list.tail = null;
		for (; i < length; i++) {
			list.head = Nothing.nothing;
			list.tail = new List();
			list = list.tail;
		}
		return this;
	}

	public List append(Object element) {
		var list = this;
		while (list.tail != null && !list.head.equals(Empty.empty))
			list = list.tail;
		list.head = element;
		list.tail = new List();
		return this;
	}

	public List extend(List element) {
		var list = this;
		while (list.tail != null && !list.head.equals(Empty.empty))
			list = list.tail;
		list.head = element.head;
		list.tail = element.tail;
		return this;
	}

	public List find(Object element) {
		var list = this;
		var result = new List();
		for (int i = 0; list != null; i++) {
			if (list.head.equals(element))
				result.append(i);
			list = list.tail;
		}

		return result;
	}

	public List set(int i, Object element) {
		var list = this;
		while (i > 0) {
			list = list.tail;
			i--;
		}
		list.head = element;
		return this;
	}

	public List replace(Object toBeReplaced, Object replacement) {
		var list = this;
		while (list != null) {
			if (list.head.equals(toBeReplaced))
				list.head = replacement;
			list = list.tail;
		}

		return this;
	}

	public List replaceFirst(Object toBeReplaced, Object replacement) {
		var list = this;
		while (list != null) {
			if (list.head.equals(toBeReplaced)) {
				list.head = replacement;
				break;
			}
			list = list.tail;
		}

		return this;
	}

	public List replaceLast(Object toBeReplaced, Object replacement) {
		var result = reversed().replaceFirst(toBeReplaced, replacement).reversed();
		head = result.head;
		tail = result.tail;
		return this;
	}

	public Object get(int i) {
		var list = this;
		while (i > 0) {
			list = list.tail;
			i--;
		}
		return list.head;
	}

	public int findFirst(Object element) {
		var list = this;
		for (int i = 0; list != null; i++) {
			if (list.head.equals(element))
				return i;
			list = list.tail;
		}
		return -1;
	}

	public int findLast(Object element) {
		return (length() - 1) - reversed().findFirst(element);
	}

	public int length() {
		var list = this;
		var counter = 0;
		while (list != null && !list.head.equals(Empty.empty)) {
			list = list.tail;
			counter++;
		}
		return counter;
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof List))
			return false;
		var otherList = (List) other;
		var list = this;
		while (list != null && otherList != null)
			if (!list.head.equals(otherList.head))
				return false;
		return true;
	}

	@Override
	public String toString() {
		var stringBuilder = new StringBuilder();
		stringBuilder.append('[');
		var list = this;
		while (true) {
			stringBuilder.append(list.head.toString());
			if (!(list.tail == null || list.tail.head.equals(Empty.empty)))
				stringBuilder.append(',');
			else {
				break;
			}
			list = list.tail;
		}
		stringBuilder.append(']');
		return stringBuilder.toString();

	}

	public List sublist(int begin, int end) {
		var list = this;
		while (begin > 0) {
			list = list.tail;
			begin--;
		}
		list = list.reversed();
		end = length() - end;
		while (end > 1) {
			list = list.tail;
			end--;
		}
		return list.reversed();
	}
}
