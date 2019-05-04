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

import ast.declarations.DeclarationNode;
import ast.declarations.FunctionDeclarationNode;
import ast.declarations.StructDeclarationNode;
import ast.declarations.VariableDeclarationNode;
import ast.expressions.OperationNode;
import lexer.Token;
import parser.LogError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Block extends NodeWithParent implements Cloneable {

    protected Block parent;
    private ArrayList<RunnableNode> children = new ArrayList<>();
    private HashMap<String, FunctionDeclarationNode> functions = new HashMap<>();
    private HashMap<String, VariableDeclarationNode> variables = new HashMap<>();
    private HashMap<String, StructDeclarationNode> structs = new HashMap<>();

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
        getFunctions().put(name, function);
    }

    public void addFunction(FunctionDeclarationNode function, String fileName, int line) {
        String name = function.getName();
        function.setFileName(fileName);
        function.setLine(line);
        if (has(name)) {
            var existing = getVariableOrFunction(name, fileName, line);
            int[] lines = {existing.getLine(), function.getLine()};
            String[] fileNames = {existing.getFileName(), function.getFileName()};
            new LogError("Variable or function with name  " + name + " already exists", fileNames, lines);
        }
        getFunctions().put(name, function);
    }

    public void addFunction(FunctionDeclarationNode function, Token token) {
        addFunction(function, token.getFileName(), token.getLine());
    }

    private void addStruct(StructDeclarationNode structure) {
        String name = structure.getName();
        if (hasStructure(name)) {
            var existing_structure = getStructs().get(name);
            new LogError("Struct " + name + " already exists", existing_structure.getFileName(),
                    existing_structure.getLine());
        }
        getStructs().put(name, structure);
    }

    public void addStruct(StructDeclarationNode structure, String fileName, int line) {
        String name = structure.getName();
        structure.setFileName(fileName);
        structure.setLine(line);
        if (hasStructure(name)) {
            var existing_structure = getStructs().get(name);
            int[] lines = {existing_structure.getLine(), structure.getLine()};
            String[] fileNames = {existing_structure.getFileName(), structure.getFileName()};
            new LogError("Struct " + name + " already exists", fileNames, lines);
        }
        getStructs().put(name, structure);
    }

    private void addVariable(VariableDeclarationNode variable) {
        String name = variable.getName();
        if (has(name))
            new LogError("Variable or function with name " + name + " already exists");
        getVariables().put(name, variable);
    }

    public void addVariable(VariableDeclarationNode variable, String fileName, int line) {
        String name = variable.getName();
        variable.setFileName(fileName);
        variable.setLine(line);
        if (has(name)) {
            var existing = getVariableOrFunction(name, fileName, line);
            int[] lines = {existing.getLine(), variable.getLine()};
            String[] fileNames = {existing.getFileName(), variable.getFileName()};
            new LogError("Variable or function with name " + name + " already exists", fileNames, lines);
        }
        getVariables().put(name, variable);
    }

    private boolean has(String name) {
        return getVariables().containsKey(name) || getFunctions().containsKey(name);
    }

    public void addVariable(VariableDeclarationNode variable, Token token) {
        addVariable(variable, token.getFileName(), token.getLine());
    }

    public void concat(Block block) {
        block.run();
        var variablesSet = block.getVariables().entrySet();
        for (Map.Entry<String, VariableDeclarationNode> entry : variablesSet)
            addVariable(entry.getValue());

        var functionsSet = block.getFunctions().entrySet();
        for (Map.Entry<String, FunctionDeclarationNode> entry : functionsSet) {
            var function = entry.getValue();
            function.getBody().setParent(this);
            addFunction(function);
        }
        var structSet = block.getStructs().entrySet();
        for (Map.Entry<String, StructDeclarationNode> entry : structSet) {
            var struct = entry.getValue();
            struct.setParent(this);
            addStruct(struct);
        }

    }

    public Block copy() {
        try {
            var copied = (Block) clone();
            copied.copyChildren();
            return copied;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void copyChildren() {
        var children = new ArrayList<RunnableNode>(getChildren().size());
        for (var child : getChildren()) {
            if (child instanceof NodeWithParent) {
                var castedChildCopy = ((NodeWithParent) child).copy();
                castedChildCopy.setParent(this);
                children.add(castedChildCopy);
            } else
                children.add(child);
        }
        setChildren(children);
    }

    public void copyVariables() {
        var variables = new HashMap<String, VariableDeclarationNode>();
        for (Map.Entry<String, VariableDeclarationNode> entry : getVariables().entrySet())
            variables.put(entry.getKey(), entry.getValue().copy());
        setVariables(variables);
    }

    public Boolean getBooleanVariableValue(String name, String fileName, int line) {
        return (Boolean) getVariable(name, fileName, line).getValue();
    }

    private ArrayList<RunnableNode> getChildren() {
        return children;
    }

    private void setChildren(ArrayList<RunnableNode> children) {
        this.children = children;
    }

    public FunctionDeclarationNode getFunction(String name, String fileName, int line) {
        Block block = this;
        while (!block.getFunctions().containsKey(name)) {
            var parent = block.getParent();
            if (parent == null) {
                block = this;
                while (!(block.getVariables().containsKey(name)
                        && block.getVariables().get(name).getValue() instanceof FunctionDeclarationNode)) {
                    parent = block.getParent();
                    if (parent == null)
                        new LogError("There isn't any variable or function with name:\t" + name, fileName, line);
                    block = parent;
                }
                return (FunctionDeclarationNode) block.getVariables().get(name).getValue();
            }
            block = parent;
        }
        return block.getFunctions().get(name);

    }

    public HashMap<String, FunctionDeclarationNode> getFunctions() {
        return functions;
    }

    protected void setFunctions(HashMap<String, FunctionDeclarationNode> functions) {
        assert functions != null;
        this.functions = functions;

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
        while (!block.getStructs().containsKey(name)) {
            var parent = block.getParent();
            if (parent == null)
                new LogError("There isn't any struct with name:\t" + name);
            block = parent;
        }
        return block.getStructs().get(name);
    }

    public StructDeclarationNode getStructure(String name, String fileName, int line) {
        Block block = this;
        while (!block.getStructs().containsKey(name)) {
            var parent = block.getParent();
            if (parent == null)
                new LogError("There isn't any struct with name:\t" + name, fileName, line);
            block = parent;
        }
        return block.getStructs().get(name);
    }

    protected HashMap<String, StructDeclarationNode> getStructs() {
        return structs;
    }

    public void setStructs(HashMap<String, StructDeclarationNode> structs) {
        assert structs != null;
        this.structs = structs;
    }

    public VariableDeclarationNode getVariable(String name, String fileName, int line) {
        Block block = this;
        while (!block.getVariables().containsKey(name)) {
            var parent = block.getParent();
            if (parent == null)
                new LogError("There isn't any variable or function with name:\t" + name, fileName, line);
            block = parent;
        }
        return block.getVariables().get(name);
    }

    public DeclarationNode getVariableOrFunction(String name, String fileName, int line) {
        Block block = this;
        while (!block.getVariables().containsKey(name)) {
            var parent = block.getParent();
            if (parent == null) {
                block = this;
                while (!block.hasFunction(name)) {
                    parent = block.getParent();
                    if (parent == null)
                        new LogError("There isn't any variable or function with name:\t" + name);
                    block = parent;
                }
                return block.getFunctions().get(name);
            }
            block = parent;
        }
        return block.getVariables().get(name);
    }

    private HashMap<String, VariableDeclarationNode> getVariables() {
        return variables;
    }

    private void setVariables(HashMap<String, VariableDeclarationNode> variables) {
        assert variables != null;
        this.variables = variables;
    }

    public Object getVariableValue(String name, String fileName, int line) {
        return getVariable(name, fileName, line).getValue();
    }

    public boolean hasFunction(String name) {
        return functions.containsKey(name);
    }

    public boolean hasStructure(String name) {
        return getStructs().containsKey(name);
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
