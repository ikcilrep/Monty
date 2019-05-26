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
    private HashMap<String, FunctionDeclarationNode> functions = new HashMap<>();
    private HashMap<String, VariableDeclarationNode> variables = new HashMap<>();
    private HashMap<String, StructDeclarationNode> structs = new HashMap<>();

    protected void setNamespaces(HashMap<String, Block> namespaces) {
        this.namespaces = namespaces;
    }

    protected HashMap<String, Block> getNamespaces() {
        return namespaces;
    }

    private HashMap<String, Block> namespaces = new HashMap<>();



    public Block(Block parent) {
        this.parent = parent;
    }

    protected HashMap<String, FunctionDeclarationNode> getFunctions() {
        return functions;
    }

    protected void setFunctions(HashMap<String, FunctionDeclarationNode> functions) {
        this.functions = functions;
    }

    protected HashMap<String, VariableDeclarationNode> getVariables() {
        return variables;
    }

    protected void setVariables(HashMap<String, VariableDeclarationNode> variables) {
        this.variables = variables;
    }

    protected HashMap<String, StructDeclarationNode> getStructs() {
        return structs;
    }

    protected void setStructs(HashMap<String, StructDeclarationNode> structs) {
        this.structs = structs;
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
            var existing = get(name,fileName,line);
            int[] lines = {existing.getLine(), line};
            String[] fileNames = {existing.getFileName(), fileName};
            new LogError("Variable or function with name  " + name + " already exists", fileNames, lines);
        }
        functions.put(name, function);
    }

    public void addFunction(FunctionDeclarationNode function, Token token) {
        addFunction(function, token.getFileName(), token.getLine());
    }

    public void addStruct(StructDeclarationNode structure, Constructor constructor, String fileName, int line) {
        String name = structure.getName();
        structure.setFileName(fileName);
        structure.setLine(line);
        if (hasStructure(name)) {
            var existing_structure = structs.get(name);
            int[] lines = {existing_structure.getLine(), line};
            String[] fileNames = {existing_structure.getFileName(), fileName};
            new LogError("Struct " + name + " already exists", fileNames, lines);
        }
        structs.put(name, structure);
        functions.put(name, constructor);
    }

    public void addVariable(VariableDeclarationNode variable, String fileName, int line) {
        String name = variable.getName();
        variable.setFileName(fileName);
        variable.setLine(line);
        if (has(name)) {
            var existing = get(name,fileName,line);
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
            var existing = get(name,fileName,line);
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

    public void concat(Block block,String fileName, int line) {
        block.run();
        var variablesSet = block.variables.entrySet();
        for (Map.Entry<String, VariableDeclarationNode> entry : variablesSet)
            addVariable(entry.getValue(), fileName, line);

        var structSet = block.structs.entrySet();
        for (Map.Entry<String, StructDeclarationNode> entry : structSet) {
            var struct = entry.getValue();
            struct.setParent(this);
            addStruct(struct,new Constructor(struct),fileName,line);
        }

        var functionsSet = block.functions.entrySet();
        for (Map.Entry<String, FunctionDeclarationNode> entry : functionsSet) {
            var function = entry.getValue();
            if (!(function instanceof Constructor)) {
                function.getBody().setParent(this);
                addFunction(function);
            }
        }

        var namespacesSet = block.namespaces.entrySet();
        for (Map.Entry<String, Block> entry : namespacesSet) {
            var namespace = entry.getValue();
            namespace.setParent(this);
            addNamespace(entry.getKey(),namespace,fileName,line);
        }
    }

    public Block copy() {
        var copied = new Block(parent);
        copied.functions = functions;
        copied.variables = variables;
        copied.children = children;
        copied.structs = structs;
        copied.namespaces = namespaces;
        copied.copyChildren();
        copied.copyVariables();
        copied.copyFunctions();
        copied.copyStructs();
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

    public void copyVariables() {
        var variables = new HashMap<String, VariableDeclarationNode>();
        for (Map.Entry<String, VariableDeclarationNode> entry : this.variables.entrySet())
            variables.put(entry.getKey(), entry.getValue().copy());
        this.variables = variables;
    }

    public void copyFunctions() {
        var functions = new HashMap<String, FunctionDeclarationNode>();
        for (Map.Entry<String, FunctionDeclarationNode> entry : this.functions.entrySet()) {
            var function = entry.getValue().copy();
            if (!(function instanceof Constructor)) {
                function.getBody().setParent(this);
                functions.put(entry.getKey(), function);
            }
        }
        this.functions = functions;
    }

    public void copyStructs() {
        var structs = new HashMap<String, StructDeclarationNode>();
        for (Map.Entry<String, StructDeclarationNode> entry : this.structs.entrySet()) {
            var struct = entry.getValue().copy();
            struct.setParent(this);
            structs.put(entry.getKey(), struct);
            this.functions.put(struct.getName(), new Constructor(struct));
        }
        this.structs = structs;
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
                return (FunctionDeclarationNode)variableValue;
        }
        if (parent == null)
            new LogError("There isn't any function with name:\t" + name, fileName, line);
        return parent.getFunction(name,fileName,line);
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

    public StructDeclarationNode getStructure(String name) {
        if (hasStructure(name))
            return structs.get(name);
        if (parent == null)
            new LogError("There isn't any struct with name:\t" + name);
        return parent.getStructure(name);
    }

    public StructDeclarationNode getStructure(String name, String fileName, int line) {
        if (hasStructure(name))
            return structs.get(name);
        if (parent == null)
            new LogError("There isn't any struct with name:\t" + name,fileName,line);
        return parent.getStructure(name,fileName,line);
    }

    public VariableDeclarationNode getVariable(String name, String fileName, int line) {
        if (hasVariable(name))
            return variables.get(name);
        if (parent == null)
            return newVariable(name,fileName,line);
        return parent.getVariable(name, fileName, line);
    }

    private VariableDeclarationNode newVariable(String name, String fileName, int line) {
        var newVariable = new VariableDeclarationNode(name);
        newVariable.setConst(Character.isUpperCase(name.charAt(0)));
        newVariable.setFileName(fileName);
        newVariable.setLine(line);
        variables.put(name,newVariable);
        return newVariable;
    }

    public Node get(String name,String fileName, int line) {
        if (hasNamespace(name))
            return namespaces.get(name);
        if (hasVariable(name))
            return variables.get(name);
        if (hasFunction(name))
            return functions.get(name);
        if (parent == null)
            return newVariable(name,fileName,line);
        return parent.get(name,fileName, line);
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
