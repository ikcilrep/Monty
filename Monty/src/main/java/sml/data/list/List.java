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

import ast.Block;
import ast.declarations.StructDeclarationNode;
import sml.data.returning.Nothing;

public final class List extends StructDeclarationNode {
	Object head = Empty.empty;
	List tail = null;

	public List() {
		super(new Block(null), "List");
		new Append(this);
		new Contains(this);
		new Extend(this);
		new Find(this);
		new FindFirst(this);
		new FindLast(this);
		new Get(this);
		new Length(this);
		new Replace(this);
		new ReplaceFirst(this);
		new ReplaceLast(this);
		new Reversed(this);
		new Set(this);
		new Sublist(this);
		new NewIterator(this);
		new Head(this);
		new Tail(this);
		new Equals(this);
	}

	public Object getHead() {
		return head;
	}

	public void setHead(Object head) {
		this.head = head;
	}

	public List getTail() {
		return tail;
	}

	public void setTail(List tail) {
		this.tail = tail;
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
		while (list != null && otherList != null) {
			if (!list.head.equals(otherList.head))
				return false;
			otherList = otherList.tail;
			list = list.tail;
		}
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
