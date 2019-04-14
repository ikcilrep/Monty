package sml.data.list;

import java.util.ArrayList;

import ast.declarations.StructDeclarationNode;
import ast.expressions.OperationNode;
import parser.LogError;
import sml.data.returning.Nothing;

public class List extends StructDeclarationNode {
	private Object[] array;
	private int length = 0;

	private void addFunctions() {
		new Add(this);
		new Count(this);
		new Extend(this);
		new Extended(this);
		new Find(this);
		new Get(this);
		new Length(this);
		new Multiplied(this);
		new Multiply(this);
		new Pop(this);
		new Remove(this);
		new Replace(this);
		new Set(this);
		new Sublist(this);

	}

	public List(ArrayList<OperationNode> arguments) {
		super(null, "[]");
		addFunctions();
		increaseCapacity((arguments.size() << 1) + 1);
		length = arguments.size();
		for (int i = 0; i < length(); i++)
			set(i, arguments.get(i).run());
	}
	
	public List(Object[] array) {
		super(null, "[]");
		addFunctions();
		increaseCapacity((array.length << 1) + 1);
		length = array.length;
		for (int i = 0; i < length(); i++)
			set(i, array[i]);
	}


	public List(List array) {
		super(null, "[]");
		addFunctions();
		this.array = new Object[array.length()];
		length = array.length();
		for (int i = 0; i < length(); i++)
			set(i, array.get(i));

	}

	public List(int length) {
		super(null, "[]");
		addFunctions();
		increaseCapacity(length << 1);
		this.length = length;
	}

	public Object[] getArray() {
		return array;
	}

	private void increaseCapacity(int capacity) {
		var newArray = new Object[capacity];
		for (int i = 0; i < length(); i++)
			newArray[i] = get(i);
		for (int i = length(); i < capacity; i++)
			newArray[i] = Nothing.nothing;
		setArray(newArray);
	}

	public void setArray(Object[] array) {
		this.array = array;
	}

	public int length() {
		return length;
	}

	public int capacity() {
		return array.length;
	}

	public List set(int index, Object value) {
		array[index] = value;
		return this;
	}

	public Object get(int index) {
		return array[index];
	}

	public List add(Object value) {
		if (length() + 1 > array.length)
			increaseCapacity(array.length << 1);
		set(length++, value);
		return this;
	}

	public List extend(List array) {
		int i = length();
		increaseCapacity((i + array.length()) << 1);
		this.length = capacity() >> 1;
		for (int j = 0; i < length(); i++, j++)
			set(i, array.get(j));

		return this;
	}

	public List extended(List array) {
		return new List(this).extend(array);
	}

	public List replace(Object toBeReplaced, Object replacement) {
		for (int i = 0; i < length(); i++)
			if (get(i).equals(toBeReplaced))
				set(i, replacement);
		return this;
	}

	public List multiplied(int times) {
		var length = length() * times;
		var array = new List(length);
		for (int i = 0; i < length; i++)
			array.set(i, get(i % length()));
		return array;
	}

	public List multiply(int times) {
		var length = length() * times;
		var array = new Object[length << 1];
		for (int i = 0; i < length; i++)
			array[i] = get(i % length());
		this.length = length;
		setArray(array);
		return this;
	}

	public int find(Object value) {
		for (int i = 0; i < length(); i++)
			if (get(i).equals(value))
				return i;
		return -1;
	}

	public List sublist(int begin, int end) {
		var sublist = new List(end - begin + 1);
		for (int i = begin, j = 0; i < end; i++, j++)
			sublist.set(j, get(i));
		return sublist;
	}
	
	public Object pop(int index) {
		var element = get(index);
		length--;
		for (int i = index; i < length(); i++)
			set(i, get(i+1));
		return element;
	}
	
	public int count(Object value) {
		int counter = 0;
		for (int i = 0; i < length(); i++)
			if (get(i).equals(value))
				counter++;
		return counter;
	}
	
	public List remove(Object value) {
		pop(find(value));
		return this;
	}
	
	public void doesHaveElement(int index, String fileName, int line) {
		if (index >= length())
			new LogError("This list doesn't have " + index + " element.", fileName, line);
	}

	public void doesCanBeExtendedWith(Object object, String fileName, int line) {
		if (!(object instanceof List))
			new LogError("Can't extend list with something that isn't list.", fileName, line);
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof List))
			return false;
		var otherList = (List) other;
		if (otherList.length() != length())
			return false;
		for (int i = 0; i < length(); i++)
			if (!get(i).equals(otherList.get(i)))
				return false;
		return true;

	}

	@Override
	public String toString() {
		var length = length();
		var stringBuilder = new StringBuilder(length << 1 + 1);
		stringBuilder.append('[');
		int i = 0;
		if (length > 0)
			while (true) {
				stringBuilder.append(get(i++).toString());
				if (i < length)
					stringBuilder.append(',');
				else
					break;
			}
		stringBuilder.append(']');
		return stringBuilder.toString();

	}

}
