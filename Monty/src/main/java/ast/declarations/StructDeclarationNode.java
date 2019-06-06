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

public class StructDeclarationNode extends Block {
    private final static HashMap<Integer, Integer> numbers = new HashMap<>();
    private static int actualStructNumber = -1;
    private final String name;
    private int structNumber;
    private int instanceNumber;

    public StructDeclarationNode(Block parent, String name) {
        super(parent);
        this.name = name;
        incrementStructNumber();
        numbers.put(structNumber, -1);
    }

    private StructDeclarationNode(Block parent, String name, int structNumber, ArrayList<RunnableNode> children,
                                  HashMap<String, VariableDeclarationNode> variables,
                                  HashMap<String, NamedFunctionDeclarationNode> functions,
                                  HashMap<String,StructDeclarationNode> structs, HashMap<String,Block> namespaces) {
        super(parent);
        this.name = name;
        this.structNumber = structNumber;
        setChildren(children);
        setVariables(variables);
        setFunctions(functions);
        setStructs(structs);
        setNamespaces(namespaces);
        numbers.put(structNumber, -1);
    }

    private void addNewStruct(Block block, String fileName, int line) {
        block.addStruct(this, new Constructor(this), fileName, line);
    }

    public void addNewStruct(Block block, Token token) {
        addNewStruct(block, token.getFileName(), token.getLine());
    }

    @Override
    public StructDeclarationNode copy() {
        StructDeclarationNode copied;
        copied = new StructDeclarationNode(getParent(), name, structNumber, getChildren(), getVariables(), getFunctions(), getStructs(),getNamespaces());
        copied.copyChildren();
        copied.copyVariables();
        copied.copyFunctions();
        copied.copyStructs();
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

    public boolean instanceOfMe(StructDeclarationNode other) {
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
