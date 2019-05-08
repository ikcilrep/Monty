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

package ast;

import ast.declarations.*;
import ast.expressions.OperationNode;
import ast.statements.ReturnStatementNode;
import lexer.Token;
import parser.LogError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Block extends NodeWithParent {

    private Block parent;

    protected void setChildren(ArrayList<RunnableNode> children) {
        this.children = children;
    }

    private ArrayList<RunnableNode> children = new ArrayList<>();
    protected HashMap<String, FunctionDeclarationNode> functions = new HashMap<>();

    protected void setVariables(HashMap<String, VariableDeclarationNode> variables) {
        this.variables = variables;
    }

    protected HashMap<String, VariableDeclarationNode> getVariables() {
        return variables;
    }

    private HashMap<String, VariableDeclarationNode> variables = new HashMap<>();
    protected HashMap<String, StructDeclarationNode> structs = new HashMap<>();

    public Block(Block parent) {
        this.parent = parent;
    }

    public void addChild(RunnableNode child) {
        children.add(child);
    }

    public void addFunction(FunctionDeclarationNode function) {
        String name = function.getName();
        if (has(name))
            new LogError("Variable or function with name  " + name + " already exists");
        functions.put(name, function);
    }

    public void addFunction(FunctionDeclarationNode function, String fileName, int line) {
        String name = function.getName();
        function.setFileName(fileName);
        function.setLine(line);
        if (has(name)) {
            var existing = getVariableOrFunction(name);
            int[] lines = {existing.getLine(), function.getLine()};
            String[] fileNames = {existing.getFileName(), function.getFileName()};
            new LogError("Variable or function with name  " + name + " already exists", fileNames, lines);
        }
        functions.put(name, function);
    }

    public void addFunction(FunctionDeclarationNode function, Token token) {
        addFunction(function, token.getFileName(), token.getLine());
    }

    private void addStruct(StructDeclarationNode struct) {
        String name = struct.getName();
        if (hasStructure(name)) {
            var existing_structure = structs.get(name);
            new LogError("Struct " + name + " already exists", existing_structure.getFileName(),
                    existing_structure.getLine());
        }
        structs.put(name, struct);
    }

    public void addStruct(StructDeclarationNode structure, Constructor constructor, String fileName, int line) {
        String name = structure.getName();
        structure.setFileName(fileName);
        structure.setLine(line);
        if (hasStructure(name)) {
            var existing_structure = structs.get(name);
            int[] lines = {existing_structure.getLine(), structure.getLine()};
            String[] fileNames = {existing_structure.getFileName(), structure.getFileName()};
            new LogError("Struct " + name + " already exists", fileNames, lines);
        }
        structs.put(name, structure);
        functions.put(name, constructor);
    }

    private void addVariable(VariableDeclarationNode variable) {
        String name = variable.getName();
        if (has(name))
            new LogError("Variable or function with name " + name + " already exists");
        variables.put(name, variable);
    }

    public void addVariable(VariableDeclarationNode variable, String fileName, int line) {
        String name = variable.getName();
        variable.setFileName(fileName);
        variable.setLine(line);
        if (has(name)) {
            var existing = getVariableOrFunction(name);
            int[] lines = {existing.getLine(), variable.getLine()};
            String[] fileNames = {existing.getFileName(), variable.getFileName()};
            new LogError("Variable or function with name " + name + " already exists", fileNames, lines);
        }
        variables.put(name, variable);
    }

    private boolean has(String name) {
        return variables.containsKey(name) || functions.containsKey(name);
    }

    public void addVariable(VariableDeclarationNode variable, Token token) {
        addVariable(variable, token.getFileName(), token.getLine());
    }

    public void concat(Block block) {
        block.run();
        var variablesSet = block.variables.entrySet();
        for (Map.Entry<String, VariableDeclarationNode> entry : variablesSet)
            addVariable(entry.getValue());

        var structSet = block.structs.entrySet();
        for (Map.Entry<String, StructDeclarationNode> entry : structSet) {
            var struct = entry.getValue();
            struct.setParent(this);
            addStruct(struct);
        }

        var functionsSet = block.functions.entrySet();
        for (Map.Entry<String, FunctionDeclarationNode> entry : functionsSet) {
            var function = entry.getValue();
            if (!(function instanceof Constructor))
                function.getBody().setParent(this);

            addFunction(function);

        }


    }

    public Block copy() {
        var copied = new Block(parent);
        copied.functions = functions;
        copied.variables = variables;
        copied.children = children;
        copied.structs = structs;
        copied.copyChildren();
        return copied;
    }

    protected void copyChildren() {
        var children = new ArrayList<RunnableNode>(this.children.size());
        for (var child : this.children) {
            if (child instanceof NodeWithParent) {
                var castedChildCopy = ((NodeWithParent) child).copy();
                castedChildCopy.setParent(this);
                children.add(castedChildCopy);
            } else
                children.add(child);
        }
        this.children = children;
    }

    public void copyVariables() {
        var variables = new HashMap<String, VariableDeclarationNode>();
        for (Map.Entry<String, VariableDeclarationNode> entry : this.variables.entrySet())
            variables.put(entry.getKey(), entry.getValue().copy());
        this.variables = variables;
    }

    protected ArrayList<RunnableNode> getChildren() {
        return children;
    }


    public FunctionDeclarationNode getFunction(String name, String fileName, int line) {
        Block block = this;
        while (!block.functions.containsKey(name)) {
            var parent = block.getParent();
            if (parent == null) {
                block = this;
                while (!(block.variables.containsKey(name)
                        && block.variables.get(name).getValue() instanceof FunctionDeclarationNode)) {
                    parent = block.getParent();
                    if (parent == null)
                        new LogError("There isn't any variable or function with name:\t" + name, fileName, line);
                    block = parent;
                }
                return (FunctionDeclarationNode) block.variables.get(name).getValue();
            }
            block = parent;
        }
        return block.functions.get(name);

    }

    public Block getParent() {
        return parent;
    }

    @Override
    public void setParent(Block parent) {
        this.parent = parent;
    }

    public String getStringVariableValue(String name, String fileName, int line) {
        return getVariable(name, fileName, line).getValue().toString();
    }

    public StructDeclarationNode getStructure(String name) {
        Block block = this;
        while (!block.structs.containsKey(name)) {
            var parent = block.getParent();
            if (parent == null)
                new LogError("There isn't any struct with name:\t" + name);
            block = parent;
        }
        return block.structs.get(name);
    }

    public StructDeclarationNode getStructure(String name, String fileName, int line) {
        Block block = this;
        while (!block.structs.containsKey(name)) {
            var parent = block.getParent();
            if (parent == null)
                new LogError("There isn't any struct with name:\t" + name, fileName, line);
            block = parent;
        }
        return block.structs.get(name);
    }

    public VariableDeclarationNode getVariable(String name, String fileName, int line) {
        Block block = this;
        while (!block.variables.containsKey(name)) {
            var parent = block.getParent();
            if (parent == null)
                new LogError("There isn't any variable or function with name:\t" + name, fileName, line);
            block = parent;
        }
        return block.variables.get(name);
    }

    private DeclarationNode getVariableOrFunction(String name) {
        Block block = this;
        while (!block.variables.containsKey(name)) {
            var parent = block.getParent();
            if (parent == null) {
                block = this;
                while (!block.hasFunction(name)) {
                    parent = block.getParent();
                    if (parent == null)
                        new LogError("There isn't any variable or function with name:\t" + name);
                    block = parent;
                }
                return block.functions.get(name);
            }
            block = parent;
        }
        return block.variables.get(name);
    }

    public DeclarationNode getVariableOrFunction(String name,String fileName, int line) {
        Block block = this;
        while (!block.variables.containsKey(name)) {
            var parent = block.getParent();
            if (parent == null) {
                block = this;
                while (!block.hasFunction(name)) {
                    parent = block.getParent();
                    if (parent == null)
                        new LogError("There isn't any variable or function with name:\t" + name,fileName,line);
                    block = parent;
                }
                return block.functions.get(name);
            }
            block = parent;
        }
        return block.variables.get(name);
    }

    public Object getVariableValue(String name, String fileName, int line) {
        return getVariable(name, fileName, line).getValue();
    }

    public boolean hasFunction(String name) {
        return functions.containsKey(name);
    }

    public boolean hasStructure(String name) {
        return structs.containsKey(name);
    }

    public boolean hasVariable(String name) {
        return variables.containsKey(name);
    }

    @Override
    public Object run() {
        Object result;
        for (RunnableNode child : children) {
            result = child.run();
            if (!(child instanceof OperationNode || result == null))
                return result;
        }
        return null;
    }
}
