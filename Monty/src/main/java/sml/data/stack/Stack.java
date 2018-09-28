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

package sml.data.stack;

public class Stack {
	Object[] array;
	int top;

	public Stack() {
		array = new Object[128];
		top = -1;
	}

	private void resize() {
		Object[] newArray = new Object[array.length << 1];
		for (int i = 0; i < array.length; i++)
			newArray[i] = array[i];
		array = newArray;
	}

	public Stack push(Object elem) {
		if (top >= array.length >>> 1)
			resize();
		array[++top] = elem;
		return this;
	}

	public Object pop() {
		return array[top--];
	}

	public Object peek() {
		return array[top];
	}

	public boolean isEmpty() {
		return top < 0;
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
