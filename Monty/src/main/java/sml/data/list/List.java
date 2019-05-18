package sml.data.list;

import ast.Block;
import ast.declarations.StructDeclarationNode;
import lexer.Lexer;
import monty.FileIO;
import monty.IOBlocks;
import parser.LogError;
import parser.parsing.Parser;
import sml.data.returning.Nothing;
import sml.data.string.MontyString;
import sml.data.tuple.Tuple;

public class List extends StructDeclarationNode {
    private Object[] array;
    private int length = 0;
    private final static Block methodsWrittenInMonty = Parser.parse(Lexer.lex(FileIO.readFile(List.class.getResourceAsStream("methods.mt"),
            "methods.mt"), "methods.mt"));

    public List() {
        super(null, "List");
    }

    public List(Tuple arguments) {
        super(null, "List");
        addFunctions();
        increaseCapacity((arguments.length() << 1) + 1);
        length = arguments.length();
        array = arguments.getArray();
    }

    public List(int length) {
        super(null, "List");
        addFunctions();
        increaseCapacity(length << 1);
        this.length = length;
    }

    public List(List array) {
        super(null, "List");
        addFunctions();
        this.array = new Object[array.length()];
        length = array.length();
        for (int i = 0; i < length(); i++)
            set(i, array.get(i));

    }

    public List(Object[] array) {
        super(null, "List");
        addFunctions();
        increaseCapacity((array.length << 1) + 1);
        length = array.length;
        for (int i = 0; i < length(); i++)
            if (array[i] instanceof String)
                set(i, new MontyString((String) array[i]));
            else
                set(i, array[i]);

    }

    public List add(Object value) {
        if (length() + 1 > array.length)
            increaseCapacity(array.length << 1);
        set(length++, value);
        return this;
    }

    private void addFunctions() {
        new Add(this);
        new Extend(this);
        new Extended(this);
        new Get(this);
        new Length(this);
        new MultipliedLeft(this);
        new Multiply(this);
        new Pop(this);
        new Set(this);
        new ToString(this);
        addFunction(new NewList());
        addVariable(IOBlocks.nothing, null, -1);
        concat(methodsWrittenInMonty);
    }

    private int capacity() {
        return array.length;
    }

    public void doesCanBeExtendedWith(Object object, String fileName, int line) {
        if (!(object instanceof List))
            new LogError("Can't extend list with something that isn't list.", fileName, line);
    }

    public void doesHaveElement(int index, String fileName, int line) {
        if (index >= length())
            new LogError("This list doesn't have " + index + " element.", fileName, line);
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

    public Object get(int index) {
        return array[index];
    }


    private void increaseCapacity(int capacity) {
        var newArray = new Object[capacity];
        for (int i = 0; i < length(); i++)
            newArray[i] = get(i);
        for (int i = length(); i < capacity; i++)
            newArray[i] = Nothing.nothing;
        this.array = newArray;
    }

    public int length() {
        return length;
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
        this.array = array;
        return this;
    }

    public Object pop(int index) {
        var element = get(index);
        length--;
        for (int i = index; i < length(); i++)
            set(i, get(i + 1));
        return element;
    }

    public List set(int index, Object value) {
        array[index] = value;
        return this;
    }

    public MontyString asString(String fileName, int line) {
        var length = length();
        var stringBuilder = new StringBuilder(length << 1 + 1);
        stringBuilder.append('[');
        int i = 0;
        if (length > 0)
            while (true) {
                var value = get(i++);
                var stringValue = sml.casts.ToString.toString(value, fileName, line);
                if (value instanceof MontyString)
                    stringBuilder.append("\"").append(stringValue).append("\"");
                else
                    stringBuilder.append(stringValue);
                if (i < length)
                    stringBuilder.append(", ");
                else
                    break;
            }
        stringBuilder.append(']');
        return new MontyString(stringBuilder.toString());

    }

}
