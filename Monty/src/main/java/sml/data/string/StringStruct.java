package sml.data.string;

import ast.declarations.StructDeclarationNode;

public class StringStruct extends StructDeclarationNode {
    private String string;

    public StringStruct(String string) {
        super(null, "String");
        this.setString(string);
        new CharAt(this);
        new ConcatLeft(this);
        new ConcatRight(this);
        new EndsWith(this);
        new EqualsIgnoreCase(this);
        new Length(this);
        new LowerCase(this);
        new MultiplyLeft(this);
        new MultiplyRight(this);
        new NewIterator(this);
        new Replace(this);
        new ReplaceFirst(this);
        new Split(this);
        new StartsWith(this);
        new Substring(this);
        new UpperCase(this);
    }

    public StringStruct mulitply(int times) {
        int length = getString().length();
        int newLength = length * times;
        char[] result = new char[newLength];
        for (int i = 0; i < newLength; i++)
            result[i] = getString().charAt(i % length);
        return new StringStruct(new String(result));
    }

    public StringStruct mulitplied(int times) {
        setString(mulitply(times).getString());
        return this;
    }

    @Override
    public String toString() {
        return getString();
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }
}
