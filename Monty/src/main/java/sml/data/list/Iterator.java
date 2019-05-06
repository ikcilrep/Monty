package sml.data.list;

import ast.declarations.StructDeclarationNode;

final class Iterator extends StructDeclarationNode {
    private final List list;
    private int counter = -1;

    Iterator(List list) {
        super(list, "Iterator");
        incrementNumber();
        this.list = list;
        new HasNext(this);
        new Next(this);
    }

    public boolean hasNext() {
        return ++counter < list.length();
    }

    public Object next() {
        return list.get(counter);
    }
}
