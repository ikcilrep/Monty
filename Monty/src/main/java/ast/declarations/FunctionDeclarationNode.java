/*
Copyright 2018 Szymon Perlicki

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

import ast.Block;
import ast.NodeTypes;
import ast.expressions.OperationNode;
import parser.DataTypes;
import parser.LogError;

public abstract class FunctionDeclarationNode extends DeclarationNode {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3950386734423560272L;
	Block body;
	public ArrayList<VariableDeclarationNode> parameters = new ArrayList<>();

	public FunctionDeclarationNode(String name, DataTypes type) {
		super(name, type);
		super.nodeType = NodeTypes.FUNCTION_DECLARATION;
	}

	public void addParameter(VariableDeclarationNode parameter) {
		parameters.add(parameter);
	}

	public abstract Object call(ArrayList<OperationNode> arguments);

	public Block getBody() {
		return body;
	}

	public ArrayList<VariableDeclarationNode> getParameters() {
		return parameters;
	}

	public void setBody(Block body) {
		this.body = body;
	}

	public void setArguments(ArrayList<OperationNode> arguments) {
		if (arguments.size() > parameters.size())
			new LogError("Too many arguments in " + name + " function call");
		else if (arguments.size() < parameters.size())
			new LogError("Too few arguments in " + name + " function call");
		for (int i = 0; i < arguments.size(); i++) {
			var name = parameters.get(i).getName();
			var dataType = parameters.get(i).getType();
			var argument = arguments.get(i);
			var value = argument.run();
			var argumentDataType = DataTypes.getDataType(value);
			if (!dataType.equals(DataTypes.ANY))
				if (!argumentDataType.equals(dataType))
					new LogError("Wrong data type for parameter with name\n\"" + name + "\" in " + getName()
							+ " function call expected " + dataType.toString().toLowerCase() + " got "
							+ argumentDataType.toString().toLowerCase());
			if (!body.doesContainVariable(name))
				body.addVariable(new VariableDeclarationNode(name, dataType));
			body.getVariableByName(name).setValue(value);
		}
	}
}
