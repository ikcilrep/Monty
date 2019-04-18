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
import ast.NodeWithParent;
import ast.RunnableNode;
import ast.expressions.OperatorOverloading;
import lexer.Token;
import sml.casts.ToBoolean;

public class StructDeclarationNode extends Block implements Cloneable {
	private static int actualStructNumber = -1;
	private static HashMap<Integer, Integer> numbers = new HashMap<>();
	private int structNumber;
	private int instanceNumber;
	private String name;
	private Constructor constructor;

	public StructDeclarationNode(Block parent, String name) {
		super(parent);
		this.name = name;
		structNumber = ++actualStructNumber;
		numbers.put(structNumber, -1);
	}

	public void addNewStruct(Block block, Token token) {
		addNewStruct(block, token.getFileName(), token.getLine());
	}


	public Constructor getConstructor() {
		return constructor;
	}

	public void addNewStruct(Block block, String fileName, int line) {
		setParent(block);
		block.addStructure(this, fileName, line);
		block.addFunction(constructor = new Constructor(this), fileName, line);
	}

	@Override
	public StructDeclarationNode copy() {
		StructDeclarationNode copied = null;
		try {
			copied = (StructDeclarationNode) clone();
			copied.constructor.setStruct(copied);
			var structs = new HashMap<String, StructDeclarationNode>();
			for (Map.Entry<String, StructDeclarationNode> entry : getStructures().entrySet()) {
				var key = entry.getKey();
				var value = entry.getValue().copy();
				value.setParent(copied);
				structs.put(key, value);
			}
			copied.setStructures(structs);

			var variables = new HashMap<String, VariableDeclarationNode>();
			for (Map.Entry<String, VariableDeclarationNode> entry : getVariables().entrySet()) {
				var key = entry.getKey();
				variables.put(key, entry.getValue().copy());
			}
			copied.setVariables(variables);

			var functions = new HashMap<String, FunctionDeclarationNode>();
			for (Map.Entry<String, FunctionDeclarationNode> entry : getFunctions().entrySet()) {
				var key = entry.getKey();
				var value = entry.getValue().copy();
				value.getBody().setParent(copied);
				functions.put(key, value);
			}
			copied.setFunctions(functions);

			var myChildren = getChildren();
			var children = new ArrayList<RunnableNode>(myChildren.size());
			for (var child : myChildren) {
				if (child instanceof NodeWithParent) {
					var castedChildCopy = ((NodeWithParent) child).copy();
					castedChildCopy.setParent(copied);
					children.add(castedChildCopy);
				} else
					children.add(child);
			}
			copied.setChildren(children);

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
		var number = numbers.get(structNumber);
		numbers.put(structNumber, number + 1);
		instanceNumber = number + 1;
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
			return function.call(new ArrayList<>(), function.getFileName(), function.getLine()).toString();
		}
		return name + "#" + getInstanceNumber();
	}

	@Override
	public boolean equals(Object other) {
		return ToBoolean.toBoolean(OperatorOverloading.overloadOperator(this, other, "$eq", 2, false),
				OperatorOverloading.getTemporaryFileName(), OperatorOverloading.getTemporaryLine());
	}
}
