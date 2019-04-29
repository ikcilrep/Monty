package sml.data.tuple;

import java.util.ArrayList;

import ast.declarations.StructDeclarationNode;
import ast.expressions.OperationNode;
import parser.LogError;
import sml.data.string.StringStruct;

public class Tuple extends StructDeclarationNode {
	private Object[] tuple;

	public Tuple(ArrayList<OperationNode> array) {
		super(null, "Tuple");
		tuple = new Object[array.size()];
		for (int i = 0; i < tuple.length; i++)
			tuple[i] = array.get(i).run();
	}

	public Object get(int index) {
		return tuple[index];
	}
	public int length() {
		return tuple.length;
	}
	
	public void doesHaveElement(int index, String fileName, int line) {
		if (index >= length())
			new LogError("This tuple doesn't have " + index + " element.", fileName, line);
	}
	
	@Override
	public String toString() {
		var length = length();
		var stringBuilder = new StringBuilder(length << 1 + 1);
		stringBuilder.append('(');
		int i = 0;
		if (length > 0)
			while (true) {
				var value = get(i++);
				if (value instanceof StringStruct)
					stringBuilder.append("\"" + value + "\"");
				else
					stringBuilder.append(value);
				if (i < length)
					stringBuilder.append(',');
				else
					break;
			}
		stringBuilder.append(')');
		return stringBuilder.toString();

	}
}
