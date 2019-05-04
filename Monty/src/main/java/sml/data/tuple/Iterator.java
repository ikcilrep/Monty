package sml.data.tuple;

import ast.declarations.StructDeclarationNode;


final class Iterator extends StructDeclarationNode {
    private Tuple tuple;
    private int counter = -1;

    Iterator(Tuple tuple) {
        super(tuple, "Iterator");
        incrementNumber();
        this.tuple = tuple;
        new HasNext(this);
        new Next(this);
    }

    public boolean hasNext() {
        return ++counter < tuple.length();
    }

    public Object next() {
        return tuple.get(counter);
    }
}
