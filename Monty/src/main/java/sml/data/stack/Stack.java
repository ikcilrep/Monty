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

package sml.data.stack;

import ast.declarations.StructDeclarationNode;
import parser.LogError;
import sml.data.array.Array;

public final class Stack extends StructDeclarationNode {

	Object[] array;
	int top;

	public Stack() {
		super(null, "Stack");
		addFunctions();
		array = new Object[128];
		top = -1;
	}

	public Stack(Object[] array) {
		super(null, "Stack");
		addFunctions();
		this.array = array;
		top = array.length - 1;
	}

	public void addFunctions() {
		new Push(this);
		new Pop(this);
		new Peek(this);
		new NewIterator(this);
		new Equals(this);
		new ToArray(this);
		new ToString(this);
		new Length(this);
	}

	@Override
	public boolean equals(Object other) {
		Stack otherStack = null;
		if (other instanceof Stack)
			otherStack = (Stack) other;
		else {
			return false;
		}
		if (otherStack.top != top)
			return false;
		for (int i = top; i >= 0; i--)
			if (!array[i].equals(otherStack.array[i]))
				return false;
		return true;
	}

	public Object[] getArray() {
		return array;
	}

	public boolean isEmpty() {
		return top < 0;
	}

	public Object peek() {
		if (top == -1)
			new LogError("This stack is empty, you can't peek with it");
		return array[top];
	}

	public Object pop() {
		if (top == -1)
			new LogError("This stack is empty, you can't pop from it");
		return array[top--];
	}

	public Stack push(Object elem) {
		if (top >= array.length >>> 1)
			resize();
		array[++top] = elem;
		return this;
	}

	private void resize() {
		Object[] newArray = new Object[array.length << 1];
		for (int i = 0; i < array.length; i++)
			newArray[i] = array[i];
		array = newArray;
	}

	public Stack reversed() {
		Object[] newArray = new Object[array.length];
		for (int j = top, i = 0; j >= 0; j--, i++)
			newArray[i] = array[j];
		Stack newStack = new Stack();
		newStack.top = top;
		newStack.array = newArray;
		return newStack;
	}

	public int length() {
		return top + 1;
	}

	public Array toArray() {
		return new Array(array).subarray(0, top + 1);
	}

	@Override
	public String toString() {
		var stringBuilder = new StringBuilder((array.length << 1) + 1);
		stringBuilder.append('[');
		for (int i = 0; i <= top; i++) {
			stringBuilder.append(array[i].toString());
			if (i < top)
				stringBuilder.append(',');
		}
		stringBuilder.append(']');
		return stringBuilder.toString();

	}

}
