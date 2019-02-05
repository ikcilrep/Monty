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

package ast.statements;

import java.util.ArrayList;

import ast.Block;
import ast.declarations.StructDeclarationNode;
import ast.declarations.VariableDeclarationNode;
import ast.expressions.OperationNode;
import parser.DataTypes;
import parser.LogError;
import sml.data.returning.BreakType;
import sml.data.returning.ContinueType;

public final  class ForStatementNode extends Block {

	private OperationNode array;
	private String variableName;

	public ForStatementNode(String variableName, OperationNode array, String fileName, int line, Block parent) {
		super(parent);
		this.variableName = variableName;
		this.array = array;
		this.fileName = fileName;
		this.line = line;
	}

	public OperationNode getArray() {
		return array;
	}

	public String getVariableName() {
		return variableName;
	}

	@Override
	public void setParent(Block parent) {
		super.setParent(parent);
		array.setParent(parent);
	}

	@Override
	public Object run() {
		Object result = null;
		var name = getVariableName();
		var isNotNameUnderscore = !name.equals("_");
		var toIter = getArray().run();
		if (toIter instanceof StructDeclarationNode) {
			var struct = (StructDeclarationNode) toIter;
			if (struct.hasFunction("Iterator")) {
				var iterator = (StructDeclarationNode) struct.getFunction("Iterator").call(new ArrayList<>(), fileName,
						line);
				if (iterator.hasFunction("hasNext") && iterator.hasFunction("next")) {
					var hasNext = iterator.getFunction("hasNext");
					var next = iterator.getFunction("next");
					if (hasNext.getType().equals(DataTypes.BOOLEAN) && !next.getType().equals(DataTypes.VOID))
						while ((boolean) hasNext.call(new ArrayList<>(), fileName, line)) {
							Object e = next.call(new ArrayList<>(), fileName, line);
							if (isNotNameUnderscore) {
								VariableDeclarationNode variable = null;
								if (hasVariable(name))
									variable = getVariable(name, getFileName(), getLine());
								else {
									variable = new VariableDeclarationNode(name, DataTypes.ANY);
									addVariable(variable);
								}
								variable.setValue(e);
							}
							result = super.run();
							if (result instanceof BreakType)
								break;
							if (result instanceof ContinueType)
								continue;
							if (result != null)
								return result;
						}
					return null;
				}
			}
		} else if (toIter instanceof String) {
			var charArray = ((String) toIter).toCharArray();
			for (char x : charArray) {
				if (isNotNameUnderscore) {
					VariableDeclarationNode variable = null;
					if (hasVariable(name))
						variable = getVariable(name, getFileName(), getLine());
					else {
						variable = new VariableDeclarationNode(name, DataTypes.ANY);
						addVariable(variable);
					}
					variable.setValue(x + "");
				}
				result = super.run();
				if (result instanceof BreakType)
					break;
				if (result instanceof ContinueType)
					continue;
				if (result != null)
					return result;
			}
			return null;
		}
		new LogError("Iterable object have to has nested struct Iterator with next and hasNext methods", fileName,
				line);
		return null;
	}
}
