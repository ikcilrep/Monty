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
import ast.expressions.OperatorOverloading;
import lexer.Token;
import sml.Sml;
import sml.casts.ToBoolean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StructDeclarationNode extends Block {
    private static int actualStructNumber = -1;
    private final static HashMap<Integer, Integer> numbers = new HashMap<>();
    private int structNumber;
    private int instanceNumber;
    private final String name;

    public StructDeclarationNode(Block parent, String name) {
        super(parent);
        this.name = name;
        incrementStructNumber();
        numbers.put(structNumber, -1);
    }

    public StructDeclarationNode(Block parent, String name, int structNumber, ArrayList<RunnableNode> children,HashMap<String,VariableDeclarationNode> variables) {
        super(parent);
        this.name = name;
        this.structNumber = structNumber;
        setChildren(children);
        setVariables(variables);
        numbers.put(structNumber, -1);
    }

    private void addNewStruct(Block block, String fileName, int line) {
        block.addStruct(this, new Constructor(this),fileName, line);
    }

    public void addNewStruct(Block block, Token token) {
        addNewStruct(block, token.getFileName(), token.getLine());
    }

    @Override
    public StructDeclarationNode copy() {
        StructDeclarationNode copied;
        copied = new StructDeclarationNode(getParent(),name,structNumber,getChildren(),getVariables());
        copied.copyChildren();
        copied.copyVariables();

        var functions = new HashMap<String, FunctionDeclarationNode>();
        for (Map.Entry<String, FunctionDeclarationNode> entry : this.functions.entrySet()) {
            var function = entry.getValue().copy();
            if (!(function instanceof Constructor)) {
                function.getBody().setParent(copied);
                functions.put(entry.getKey(), function);
            }
        }
        copied.functions = functions;

        var structs = new HashMap<String, StructDeclarationNode>();
        for (Map.Entry<String, StructDeclarationNode> entry : this.structs.entrySet()) {
            var value = entry.getValue().copy();
            value.setParent(copied);
            structs.put(entry.getKey(), value);
            copied.functions.put(value.getName(), new Constructor(value));
        }
        copied.structs = structs;
        return copied;
    }

    @Override
    public boolean equals(Object other) {
        return ToBoolean.toBoolean(OperatorOverloading.overloadOperator(this, other, "$eq", 2),
                OperatorOverloading.getTemporaryFileName(), OperatorOverloading.getTemporaryLine());
    }


    public String getName() {
        return name;
    }


    protected void incrementNumber() {
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

    @Override
    public String toString() {
        if (hasFunction("toString")) {
            var function = getFunction("toString", OperatorOverloading.getTemporaryFileName(), OperatorOverloading.getTemporaryLine());
            return function.call(Sml.EMPTY_ARGUMENT_LIST, function.getFileName(), function.getLine()).toString();
        }
        return name + "#" + instanceNumber;
    }
}
