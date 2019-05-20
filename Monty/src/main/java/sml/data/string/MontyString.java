package sml.data.string;

import ast.Block;
import ast.declarations.StructDeclarationNode;
import lexer.Lexer;
import monty.FileIO;
import parser.LogError;
import parser.parsing.Parser;
import sml.data.list.List;

public class MontyString extends StructDeclarationNode {
    private String string;
    private final static Block methodsWrittenInMonty = Parser.parse(Lexer.lex(FileIO.readFile(MontyString.class.getResourceAsStream("methods.mt"),
            "methods.mt"), "methods.mt"));

    public MontyString(String string) {
        super(null, "String");
        this.setString(string);
        new CharAt(this);
        new AddedLeft(this);
        new AddedRight(this);
        new AddLeft(this);
        new AddRight(this);
        new EndsWith(this);
        new Equals(this);
        new EqualsIgnoreCase(this);
        new Length(this);
        new LowerCase(this);
        new MultipliedLeft(this);
        new MultiplyLeft(this);
        new Replace(this);
        new ReplaceFirst(this);
        new Split(this);
        new StartsWith(this);
        new Substring(this);
        new UpperCase(this);
        new ToString(this);
        concat(methodsWrittenInMonty.copy());
    }

    MontyString multiplied(int times) {
        int length = getString().length();
        int newLength = length * times;
        char[] result = new char[newLength];
        for (int i = 0; i < newLength; i++)
            result[i] = getString().charAt(i % length);
        return new MontyString(new String(result));
    }

    MontyString multiply(int times) {
        string = multiplied(times).string;
        return this;
    }

    MontyString added(MontyString other) {
        return new MontyString(string + other.string);
    }

    public MontyString add(MontyString other) {
        string += other.string;
        return this;
    }


    MontyString charAt(int index, String fileName, int line) {
        try {
            return new MontyString(String.valueOf(string.charAt(index)));
        } catch (IndexOutOfBoundsException e) {
            new LogError("Index " + index + " out of bounds for length " + string.length(), fileName, line);
        }
        return null;
    }

    MontyString replace(MontyString regex, MontyString replacement) {
        return new MontyString(string.replaceAll(regex.string, replacement.string));
    }

    MontyString replaceFirst(MontyString regex, MontyString replacement) {
        return new MontyString(string.replaceFirst(regex.string, replacement.string));
    }

    List split(MontyString regex) {
        var split = string.split(regex.string);
        var splitAndCasted = new MontyString[split.length];
        var i = 0;
        for (var string : split)
            splitAndCasted[i++] = new MontyString(string);
        return new List(splitAndCasted);
    }

    boolean endsWith(MontyString suffix) {
        return string.endsWith(suffix.string);
    }
    boolean startsWith(MontyString prefix) {
        return string.startsWith(prefix.string);
    }

    MontyString lowerCase() {
        return new MontyString(string.toLowerCase());
    }
    MontyString upperCase() {
        return new MontyString(string.toUpperCase());
    }

    MontyString substring(int begin, int end, String fileName, int line) {
        if (begin < 0)
            new LogError("Begin can't be negative.", fileName, line);
        if (end > length())
            new LogError("End can't be greater than length of list.", fileName, line);
        if (begin > end)
            new LogError("Begin can't be greater or equals to end.", fileName, line);
        return new MontyString(string.substring(begin, end));
    }
    int length() {
        return string.length();
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof MontyString))
            return false;
        return ((MontyString) other).string.equals(string);
    }

    @Override
    public MontyString toString(String fileName, int line) {
        return this;
    }

    @Override
    public String toString() {
        return string;
    }

    public String getString() {
        return string;
    }

    private void setString(String string) {
        this.string = string;
    }
}
