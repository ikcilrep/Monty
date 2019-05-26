package sml.data.tuple;

import ast.Block;
import ast.declarations.StructDeclarationNode;
import ast.declarations.VariableDeclarationNode;
import lexer.Lexer;
import monty.FileIO;
import parser.LogError;
import parser.parsing.Parser;
import sml.data.string.MontyString;
import sml.io.Println;

import java.util.LinkedList;

public class Tuple extends StructDeclarationNode {
    private final static Block methodsWrittenInMonty;

    static {
        methodsWrittenInMonty = Parser.parse(Lexer.lex(FileIO.readFile(Tuple.class.getResourceAsStream("methods.mt"),
                "tuple/methods.mt"), "tuple/methods.mt"));
    }

    private final Object[] array;

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
    public Tuple(Object[] array) {
        super(null, "Tuple");
        addFunctions();
        this.array = array;

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
        addFunctions();
        array = new Object[0];
    }

    private void addFunctions() {
        new Get(this);
        new Length(this);
        new ToString(this);
        addFunction(new Println());
        concat(methodsWrittenInMonty.copy());
    }

    public Object get(int index) {
        var result = array[index];
        if (result instanceof VariableDeclarationNode)
            return ((VariableDeclarationNode) result).getValue();
        return result;
    }
    public Object justGet(int index) {
        return array[index];
    }

    public int length() {
        return array.length;
    }

    public void doesHaveElement(int index, String fileName, int line) {
        if (index >= length())
            new LogError("This array doesn't have " + index + " element.", fileName, line);
    }

    public MontyString asString(String fileName, int line) {
        var length = length();
        var stringBuilder = new StringBuilder(length << 1 + 1);
        stringBuilder.append('(');
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
        stringBuilder.append(')');
        return new MontyString(stringBuilder.toString());

    }
}
