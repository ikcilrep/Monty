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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ast.Block;
import ast.Node;
import ast.NodeTypes;
import ast.NodeWithParent;
import ast.expressions.ConstantNode;
import ast.expressions.OperationNode;
import lexer.Token;
import parser.DataTypes;
import parser.LogError;

public class StructDeclarationNode extends Block implements Cloneable {
	private static int actualStructNumber = -1;
	private static int number = -1;
	private int structNumber;
	private int instanceNumber;
	private String name;

	public StructDeclarationNode(Block parent, String name) {
		super(parent);
		super.nodeType = NodeTypes.STRUCT_DECLARATION;
		this.name = name;
		structNumber = ++actualStructNumber;
	}

	public void addNewStruct(Block block, Token token) {
		var struct = this;
		var newStructFunction = new FunctionDeclarationNode(name, DataTypes.ANY) {

			@Override
			public Object call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
				var newStruct = struct.copy();
				var thisVariable = new VariableDeclarationNode("this", DataTypes.ANY);
				thisVariable.setValue(newStruct);
				newStruct.addVariable(thisVariable);
				if (newStruct.hasFunction("init")) {
					var function = newStruct.getFunction("init");
					if (!function.getType().equals(DataTypes.VOID)) {
						String[] fileNames = { function.getFileName(), callFileName };
						int[] lines = { function.getLine(), callLine };
						new LogError("Init method have to be void", fileNames, lines);
					}
					function.call(arguments, callFileName, callLine);
				}
				newStruct.incrementNumber();
				return newStruct;
			}
		};
		newStructFunction.setBody(new Block(null));
		block.addFunction(newStructFunction, token);
		var checkingFunction = new FunctionDeclarationNode("is" + name, DataTypes.BOOLEAN) {

			@Override
			public Object call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
				setArguments(arguments, callFileName, callLine);
				var other = getBody().getVariable("other").getValue();
				if (other instanceof StructDeclarationNode)
					return struct.instanceOfMe(((StructDeclarationNode) other));
				return false;
			}

		};
		checkingFunction.setBody(new Block(null));
		checkingFunction.addParameter("other", DataTypes.ANY);
		block.addFunction(checkingFunction, token);
	}

	@Override
	public StructDeclarationNode copy() {
		StructDeclarationNode copied = null;
		try {
			copied = (StructDeclarationNode) clone();
			var variables = new HashMap<String, VariableDeclarationNode>();
			var variablesSet = copied.getVariables().entrySet();
			for (Map.Entry<String, VariableDeclarationNode> entry : variablesSet) {
				var key = entry.getKey();
				variables.put(key, entry.getValue().copy());
			}
			copied.setVariables(variables);
			var functions = new HashMap<String, FunctionDeclarationNode>();
			var functionsSet = copied.getFunctions().entrySet();
			for (Map.Entry<String, FunctionDeclarationNode> entry : functionsSet) {
				var key = entry.getKey();
				var value = entry.getValue().copy();
				var body = value.getBody().copy();
				body.setParent(copied);
				value.setBody(body);
				@SuppressWarnings("unchecked")
				var children = (ArrayList<Node>) body.getChildren().clone();
				for (int i = 0; i < children.size(); i++) {
					if (children.get(i) instanceof NodeWithParent) {
						var castedChildCopy = ((NodeWithParent) children.get(i)).copy();
						children.set(i, castedChildCopy);
						castedChildCopy.setParent(body);
					}
				}
				body.setChildren(children);
				functions.put(key, value);
			}
			copied.setFunctions(functions);

		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return copied;
	}

	public int getInstanceNumber() {
		return instanceNumber;
	}

	public String getName() {
		return name;
	}

	public int getStructNumber() {
		return structNumber;
	}

	public void incrementNumber() {
		instanceNumber = ++number;
	}

	public boolean instanceOfMe(StructDeclarationNode s) {
		return s.getStructNumber() == structNumber;
	}

	public void setFunctions(HashMap<String, FunctionDeclarationNode> functions) {
		this.functions = functions;
	}

	@Override
	public String toString() {
		if (hasFunction("toString")) {
			var function = getFunction("toString");
			if (!function.getType().equals(DataTypes.STRING))
				new LogError("Function toString have to return string", function.getFileName(), function.getLine());
			if (function.getParameters().size() != 0)
				new LogError("Function toString mustn't have any parameter", function.getFileName(),
						function.getLine());
			return function.call(new ArrayList<>(), function.getFileName(), function.getLine()).toString();
		}
		return name + "#" + getInstanceNumber();
	}

	@Override
	public boolean equals(Object other) {
		if (hasFunction("equals")) {
			var function = getFunction("equals");
			if (!function.getType().equals(DataTypes.BOOLEAN))
				new LogError("Function equals have to return boolean", function.getFileName(), function.getLine());
			if (function.getParameters().size() != 1)
				new LogError("Function equals mustn't have more or less than one parameter", function.getFileName(),
						function.getLine());
			if (!function.getParameters().get(0).getType().equals(DataTypes.ANY))
				new LogError("Function equals have to has parameter with \"any\" data type", function.getFileName(),
						function.getLine());
			var arguments = new ArrayList<OperationNode>();
			arguments.add(new OperationNode(new ConstantNode(other, DataTypes.ANY), new Block(null)));
			return (boolean) function.call(arguments, function.getFileName(), function.getLine());
		}
		return false;
	}

}
