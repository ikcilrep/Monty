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
import lexer.Token;
import parser.LogError;
import sml.casts.ToString;
import sml.data.string.MontyString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Block extends NodeWithParent {
    private Block parent;
    private ArrayList<RunnableNode> children = new ArrayList<>();
    private HashMap<String, NamedFunctionDeclarationNode> functions = new HashMap<>();
    private HashMap<String, VariableDeclarationNode> variables = new HashMap<>();
    private HashMap<String, TypeDeclarationNode> types = new HashMap<>();
    private HashMap<String, Block> namespaces = new HashMap<>();

    public Block(Block parent) {
        this.parent = parent;
    }

    protected HashMap<String, Block> getNamespaces() {
        return namespaces;
    }

    protected void setNamespaces(HashMap<String, Block> namespaces) {
        this.namespaces = namespaces;
    }

    protected HashMap<String, NamedFunctionDeclarationNode> getFunctions() {
        return functions;
    }

    protected void setFunctions(HashMap<String, NamedFunctionDeclarationNode> functions) {
        this.functions = functions;
    }

    protected HashMap<String, TypeDeclarationNode> getTypes() {
        return types;
    }

    protected void setTypes(HashMap<String, TypeDeclarationNode> types) {
        this.types = types;
    }

    public void addChild(RunnableNode child) {
        children.add(child);
    }

    public void addFunction(NamedFunctionDeclarationNode function) {
        String name = function.getName();
        if (has(name))
            new LogError("Variable or function with name  " + name + " already exists");
        functions.put(name, function);
    }

    public void addFunction(NamedFunctionDeclarationNode function, String fileName, int line) {
        String name = function.getName();
        function.setFileName(fileName);
        function.setLine(line);
        if (has(name)) {
            var existing = get(name, fileName, line);
            int[] lines = {existing.getLine(), line};
            String[] fileNames = {existing.getFileName(), fileName};
            new LogError("Variable or function with name  " + name + " already exists", fileNames, lines);
        }
        functions.put(name, function);
    }

    public void addFunction(NamedFunctionDeclarationNode function, Token token) {
        addFunction(function, token.getFileName(), token.getLine());
    }

    public void addType(TypeDeclarationNode type, Constructor constructor, String fileName, int line) {
        String name = type.getName();
        type.setFileName(fileName);
        type.setLine(line);
        if (hasType(name)) {
            var existing_type = types.get(name);
            int[] lines = {existing_type.getLine(), line};
            String[] fileNames = {existing_type.getFileName(), fileName};
            new LogError("Type with name" + name + " already exists", fileNames, lines);
        }
        types.put(name, type);
        functions.put(name, constructor);
    }

    public void addVariable(VariableDeclarationNode variable, String fileName, int line) {
        String name = variable.getName();
        variable.setFileName(fileName);
        variable.setLine(line);
        if (has(name)) {
            var existing = get(name, fileName, line);
            int[] lines = {existing.getLine(), line};
            String[] fileNames = {existing.getFileName(), fileName};
            new LogError("Variable or function with name " + name + " already exists", fileNames, lines);
        }
        variables.put(name, variable);
    }

    public void addNamespace(String name, Block namespace, String fileName, int line) {
        namespace.setFileName(fileName);
        namespace.setLine(line);
        if (hasNamespace(name)) {
            var existing = get(name, fileName, line);
            int[] lines = {existing.getLine(), line};
            String[] fileNames = {existing.getFileName(), fileName};
            new LogError("Namespace with name " + name + " already exists", fileNames, lines);
        }
        namespaces.put(name, namespace);
    }

    public boolean hasNamespace(String name) {
        return namespaces.containsKey(name);
    }

    private boolean has(String name) {
        return variables.containsKey(name) || functions.containsKey(name);
    }

    public void concat(Block block, String fileName, int line) {
        block.run();

        var structSet = block.types.entrySet();
        for (Map.Entry<String, TypeDeclarationNode> entry : structSet) {
            var type = entry.getValue();
            type.setParent(this);
            addType(type, new Constructor(type), fileName, line);
        }

        var functionsSet = block.functions.entrySet();
        for (Map.Entry<String, NamedFunctionDeclarationNode> entry : functionsSet) {
            var function = entry.getValue();
            if (!(function instanceof Constructor)) {
                function.getBody().setParent(this);
                addFunction(function);
            }
        }

        var variablesSet = block.variables.entrySet();
        for (Map.Entry<String, VariableDeclarationNode> entry : variablesSet)
            addVariable(entry.getValue(), fileName, line);


        var namespacesSet = block.namespaces.entrySet();
        for (Map.Entry<String, Block> entry : namespacesSet) {
            var namespace = entry.getValue();
            namespace.setParent(this);
            addNamespace(entry.getKey(), namespace, fileName, line);
        }
    }

    public Block copy() {
        var copied = new Block(parent);
        copied.functions = functions;
        copied.variables = variables;
        copied.children = children;
        copied.types = types;
        copied.namespaces = namespaces;
        copied.copyChildren();
        copied.copyFunctions();
        copied.copyTypes();
        copied.copyVariables();
        copied.copyNamespaces();
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

    private void copyVariables() {
        var variables = new HashMap<String, VariableDeclarationNode>();
        for (Map.Entry<String, VariableDeclarationNode> entry : this.variables.entrySet())
            variables.put(entry.getKey(), entry.getValue().copy());

        this.variables = variables;
    }

    public void copyFunctions() {
        var functions = new HashMap<String, NamedFunctionDeclarationNode>();
        for (Map.Entry<String, NamedFunctionDeclarationNode> entry : this.functions.entrySet()) {
            var function = entry.getValue().copy();
            if (!(function instanceof Constructor)) {
                function.getBody().setParent(this);
                functions.put(entry.getKey(), function);
            }
        }
        this.functions = functions;
    }

    public void copyTypes() {
        var types = new HashMap<String, TypeDeclarationNode>();
        for (Map.Entry<String, TypeDeclarationNode> entry : this.types.entrySet()) {
            var type = entry.getValue().copy();
            type.setParent(this);
            types.put(entry.getKey(), type);
            this.functions.put(type.getName(), new Constructor(type));
        }
        this.types = types;
    }

    public void copyNamespaces() {
        var namespaces = new HashMap<String, Block>();
        for (Map.Entry<String, Block> entry : this.namespaces.entrySet()) {
            var namespace = entry.getValue().copy();
            namespace.setParent(this);
            namespaces.put(entry.getKey(), namespace);
        }
        this.namespaces = namespaces;
    }

    protected ArrayList<RunnableNode> getChildren() {
        return children;
    }

    protected void setChildren(ArrayList<RunnableNode> children) {
        this.children = children;
    }

    public FunctionDeclarationNode getFunction(String name, String fileName, int line) {
        if (hasFunction(name))
            return functions.get(name);
        if (hasVariable(name)) {
            var variableValue = variables.get(name).getValue();
            if (variableValue instanceof FunctionDeclarationNode)
                return (FunctionDeclarationNode) variableValue;
        }
        if (parent == null)
            new LogError("There isn't any function with name:\t" + name, fileName, line);
        return parent.getFunction(name, fileName, line);
    }

    public NamedFunctionDeclarationNode getNamedFunction(String name, String fileName, int line) {
        if (hasFunction(name))
            return functions.get(name);
        if (parent == null)
            new LogError("There isn't any function with name:\t" + name, fileName, line);
        return parent.getNamedFunction(name, fileName, line);
    }

    public Block getParent() {
        return parent;
    }

    @Override
    public void setParent(Block parent) {
        this.parent = parent;
    }

    public MontyString getStringVariableValue(String name, String fileName, int line) {
        return ToString.toString(getVariable(name, fileName, line).getValue(), fileName, line);
    }

    public TypeDeclarationNode getType(String name) {
        if (hasType(name))
            return types.get(name);
        if (parent == null)
            new LogError("There isn't any type with name:\t" + name);
        return parent.getType(name);
    }

    public TypeDeclarationNode getType(String name, String fileName, int line) {
        if (hasType(name))
            return types.get(name);
        if (parent == null)
            new LogError("There isn't any type with name:\t" + name, fileName, line);
        return parent.getType(name, fileName, line);
    }

    public VariableDeclarationNode getVariable(String name, String fileName, int line) {
        var result = partOfRecursionGetVariable(name);
        return result == null ? newVariable(name, fileName, line) : result;
    }

    private VariableDeclarationNode newVariable(String name, String fileName, int line) {
        var newVariable = new VariableDeclarationNode(name);
        newVariable.setConst(Character.isUpperCase(name.charAt(0)));
        newVariable.setFileName(fileName);
        newVariable.setLine(line);
        variables.put(name, newVariable);
        return newVariable;
    }

    public Node get(String name, String fileName, int line) {
        var result = partOfRecursionGet(name);
        return result == null ? newVariable(name, fileName, line) : result;
    }

    private Node partOfRecursionGet(String name) {
        if (hasNamespace(name))
            return namespaces.get(name);
        if (hasVariable(name))
            return variables.get(name);
        if (hasFunction(name))
            return functions.get(name);
        if (parent == null)
            return null;
        return parent.partOfRecursionGet(name);
    }

    private VariableDeclarationNode partOfRecursionGetVariable(String name) {
        if (hasVariable(name))
            return variables.get(name);
        if (parent == null)
            return null;
        return parent.partOfRecursionGetVariable(name);
    }

    public Block getNamespace(String name) {
        return namespaces.get(name);
    }

    public Object getVariableValue(String name, String fileName, int line) {
        return getVariable(name, fileName, line).getValue();
    }

    public boolean hasFunction(String name) {
        return functions.containsKey(name);
    }

    public boolean hasType(String name) {
        return types.containsKey(name);
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
