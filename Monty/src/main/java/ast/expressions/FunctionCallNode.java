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

package ast.expressions;

import java.util.ArrayList;

import ast.NodeTypes;

public class FunctionCallNode extends ExpressionNode implements StructContainer {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6176257179629588339L;
	private String name;
	private ArrayList<OperationNode> arguments = new ArrayList<>();
	private OperationNode next;

	public FunctionCallNode(String name) {
		this.name = name;
		super.nodeType = NodeTypes.FUNCTION_CALL;
	}

	public void addArgument(OperationNode argument) {
		arguments.add(argument);
	}

	public ArrayList<OperationNode> getArguments() {
		return arguments;
	}

	public String getName() {
		return name;
	}

	@Override
	public OperationNode getNext() {
		return next;
	}

	@Override
	public void setNext(OperationNode variableOrFunction) {
		next = variableOrFunction;
	}

}
