package sml.data.string;

import ast.Block;
import ast.declarations.StructDeclarationNode;
import lexer.Lexer;
import monty.FileIO;
import parser.parsing.Parser;

public class StringStruct extends StructDeclarationNode {
    private String string;
    private final static Block methodsWrittenInMonty = Parser.parse(Lexer.lex(FileIO.readFile(StringStruct.class.getResourceAsStream("methods.mt"),
            "methods.mt"), "methods.mt"));

    public StringStruct(String string) {
        super(null, "String");
        this.setString(string);
        new CharAt(this);
        new AddedLeft(this);
        new AddedRight(this);
        new AddLeft(this);
        new AddRight(this);
        new EndsWith(this);
        new EqualsIgnoreCase(this);
        new Length(this);
        new LowerCase(this);
        new MultipliedLeft(this);
        new MultipliedRight(this);
        new MultiplyLeft(this);
        new MultiplyRight(this);
        new Replace(this);
        new ReplaceFirst(this);
        new Split(this);
        new StartsWith(this);
        new Substring(this);
        new UpperCase(this);
        concat(methodsWrittenInMonty);
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
