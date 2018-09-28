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
