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
import ast.expressions.OperatorOverloading;
import lexer.Token;
import sml.Sml;
import sml.casts.ToBoolean;

import java.util.HashMap;
import java.util.Map;

public class StructDeclarationNode extends Block implements Cloneable {
    private static int actualStructNumber = -1;
    private static HashMap<Integer, Integer> numbers = new HashMap<>();
    private int structNumber;
    private int instanceNumber;
    private String name;

    public StructDeclarationNode(Block parent, String name) {
        super(parent);
        setName(name);
        incrementStructNumber();
        numbers.put(structNumber, -1);
    }

    private void addNewStruct(Block block, String fileName, int line) {
        block.addStruct(this, fileName, line);
        block.addFunction(new Constructor(this), fileName, line);
    }

    public void addNewStruct(Block block, Token token) {
        addNewStruct(block, token.getFileName(), token.getLine());
    }

    @Override
    public StructDeclarationNode copy() {
        StructDeclarationNode copied;
        copied = (StructDeclarationNode) super.copy();
        var structs = new HashMap<String, StructDeclarationNode>();
        for (Map.Entry<String, StructDeclarationNode> entry : getStructs().entrySet()) {
            var value = entry.getValue().copy();
            value.setParent(copied);
            structs.put(entry.getKey(), value);
            copied.getFunctions().put(value.getName(), new Constructor(value));
        }
        copied.setStructs(structs);

        copied.copyVariables();

        var functions = new HashMap<String, FunctionDeclarationNode>();
        for (Map.Entry<String, FunctionDeclarationNode> entry : getFunctions().entrySet()) {
            var value = entry.getValue().copy();
            value.getBody().setParent(copied);
            functions.put(entry.getKey(), value);
        }
        copied.setFunctions(functions);
        return copied;
    }

    @Override
    public boolean equals(Object other) {
        return ToBoolean.toBoolean(OperatorOverloading.overloadOperator(this, other, "$eq", 2, false),
                OperatorOverloading.getTemporaryFileName(), OperatorOverloading.getTemporaryLine());
    }

    private int getInstanceNumber() {
        return instanceNumber;
    }

    public String getName() {
        return name;
    }

    private int getStructNumber() {
        return structNumber;
    }

    protected void incrementNumber() {
        var number = numbers.get(structNumber);
        numbers.put(structNumber, number + 1);
        instanceNumber = number + 1;
    }

    private void incrementStructNumber() {
        this.structNumber = ++actualStructNumber;
    }

    public boolean instanceOfMe(StructDeclarationNode s) {
        return s.getStructNumber() == structNumber;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        if (hasFunction("toString")) {
            var function = getFunction("toString", OperatorOverloading.getTemporaryFileName(), OperatorOverloading.getTemporaryLine());
            return function.call(Sml.emptyArgumentList, function.getFileName(), function.getLine()).toString();
        }
        return name + "#" + getInstanceNumber();
    }
}
