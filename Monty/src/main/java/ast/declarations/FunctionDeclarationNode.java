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
import java.util.Arrays;
import java.util.LinkedList;

import ast.Block;
import ast.NodeTypes;
import ast.expressions.OperationNode;
import parser.DataTypes;
import parser.LogError;

public abstract class FunctionDeclarationNode extends DeclarationNode implements Cloneable {
	Block body;

	public ArrayList<VariableDeclarationNode> parameters = new ArrayList<>();

	public FunctionDeclarationNode(String name, DataTypes type) {
		super(name, type);
		super.nodeType = NodeTypes.FUNCTION_DECLARATION;
	}

	public void addParameter(VariableDeclarationNode parameter) {
		parameters.add(parameter);
	}

	public abstract Object call(ArrayList<OperationNode> arguments, String callFileName, int callLine);

	public Block getBody() {
		return body;
	}

	public ArrayList<VariableDeclarationNode> getParameters() {
		return parameters;
	}

	public void setArguments(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
		if (arguments.size() > parameters.size())
			new LogError("Too many arguments in " + name + " function call", callFileName, callLine);
		else if (arguments.size() < parameters.size())
			new LogError("Too few arguments in " + name + " function call", callFileName, callLine);
		var runnedArguments = new ArrayList<Object>(Arrays.asList(new Object[arguments.size()]));

		for (int i = 0; i < arguments.size(); i++) {
			var dataType = parameters.get(i).getType();
			var argument = arguments.get(i);
			var value = argument.run();
			var argumentDataType = DataTypes.getDataType(value);
			if (!dataType.equals(DataTypes.ANY))
				if (!argumentDataType.equals(dataType))
					new LogError("Wrong data type for" + i + "parameter with name\n\"" + name + "\" in " + getName()
							+ " function call expected " + dataType.toString().toLowerCase() + " got "
							+ argumentDataType.toString().toLowerCase(), callFileName, callLine);

			runnedArguments.set(i, value);
		}
		for (int i = 0; i < runnedArguments.size(); i++) {
			var name = parameters.get(i).getName();
			var dataType = parameters.get(i).getType();
			VariableDeclarationNode variable = null;
			if (!body.doesContainVariable(name)) {
				variable = new VariableDeclarationNode(name, dataType);
				body.addVariable(variable, fileName, line);
			} else
				variable = body.getVariableByName(name, getFileName(), getLine());
			variable.setValue(runnedArguments.get(i));
		}
	}

	public void setBody(Block body) {
		this.body = body;
	}

	public FunctionDeclarationNode copy() {
		try {
			return (FunctionDeclarationNode) clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}
}
