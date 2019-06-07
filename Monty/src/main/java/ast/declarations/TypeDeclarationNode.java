/*
	Copyright 2018-2019 Szymon Perlicki

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package ast.declarations;

import ast.Block;
import ast.RunnableNode;
import ast.expressions.OperationNode;
import lexer.Token;
import sml.data.string.MontyString;

import java.util.ArrayList;
import java.util.HashMap;

public class TypeDeclarationNode extends Block {
    private final static HashMap<Integer, Integer> numbers = new HashMap<>();
    private static int actualStructNumber = -1;
    private final String name;
    private int structNumber;
    private int instanceNumber;

    public TypeDeclarationNode(Block parent, String name) {
        super(parent);
        this.name = name;
        incrementStructNumber();
        numbers.put(structNumber, -1);
    }

    private TypeDeclarationNode(Block parent, String name, int structNumber, ArrayList<RunnableNode> children,
                                HashMap<String, NamedFunctionDeclarationNode> functions,
                                HashMap<String, TypeDeclarationNode> structs, HashMap<String, Block> namespaces) {
        super(parent);
        this.name = name;
        this.structNumber = structNumber;
        setChildren(children);
        setFunctions(functions);
        setTypes(structs);
        setNamespaces(namespaces);
        numbers.put(structNumber, -1);
    }

    private void addNewType(Block block, String fileName, int line) {
        block.addType(this, new Constructor(this), fileName, line);
    }

    public void addNewType(Block block, Token token) {
        addNewType(block, token.getFileName(), token.getLine());
    }

    @Override
    public TypeDeclarationNode copy() {
        TypeDeclarationNode copied;
        copied = new TypeDeclarationNode(getParent(), name, structNumber, getChildren(), getFunctions(), getTypes(), getNamespaces());
        copied.copyChildren();
        copied.copyFunctions();
        copied.copyTypes();
        copied.copyNamespaces();
        return copied;
    }


    public String getName() {
        return name;
    }


    void incrementNumber() {
        var number = numbers.get(structNumber);
        numbers.put(structNumber, number + 1);
        instanceNumber = number + 1;
    }

    private void incrementStructNumber() {
        this.structNumber = ++actualStructNumber;
    }

    public boolean instanceOfMe(TypeDeclarationNode other) {
        return other.structNumber == structNumber;
    }

    public MontyString toString(String fileName, int line) {
        String text;
        if (hasFunction("$str"))
            text = getFunction("$str", fileName, line).call(OperationNode.emptyTuple, fileName, line).toString();
        else
            text = name + "#" + instanceNumber;
        return new MontyString(text);
    }

    public void addThisVariable(String fileName, int line) {
        var thisVariable = new VariableDeclarationNode("This");
        thisVariable.setValue(this, fileName, line);
        thisVariable.setConst(true);
        addVariable(thisVariable, fileName, line);
    }
}
