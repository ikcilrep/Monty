package sml.data.tuple;

import ast.declarations.StructDeclarationNode;
import ast.declarations.VariableDeclarationNode;
import parser.LogError;
import sml.data.string.StringStruct;

import java.util.LinkedList;

public class Tuple extends StructDeclarationNode {
    public Object[] getArray() {
        return array;
    }

    private final Object[] array;

    private void addFunctions() {
         new NewIterator(this);
         new Get(this);
    }


    public Tuple(LinkedList list) {
        super(null, "Tuple");
        addFunctions();
        this.array = list.toArray();
    }
    public Tuple(Object elem) {
        super(null, "Tuple");
        addFunctions();
        array = new Object[1];
        array[0] = elem;
    }
    public Tuple(Object elem1, Object elem2) {
        super(null, "Tuple");
        addFunctions();
        array = new Object[2];
        array[0] = elem1;
        array[1] = elem2;

    }
    public Tuple() {
        super(null, "Tuple");
        array = new Object[0];
    }
    public Object get(int index) {
        if (array[index] instanceof VariableDeclarationNode)
            return ((VariableDeclarationNode) array[index]).getValue();
        return array[index];
    }

    public int length() {
        return array.length;
    }

    public void doesHaveElement(int index, String fileName, int line) {
        if (index >= length())
            new LogError("This array doesn't have " + index + " element.", fileName, line);
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
                    stringBuilder.append("\"").append(value).append("\"");
                else
                    stringBuilder.append(value);
                if (i < length)
                    stringBuilder.append(", ");
                else
                    break;
            }
        stringBuilder.append(')');
        return stringBuilder.toString();

    }
}
