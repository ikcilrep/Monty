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


public final class FunctionCallNode implements StructContainer {

	private String name;
	private ArrayList<OperationNode> arguments = new ArrayList<>();
	private OperationNode next;

	public FunctionCallNode(String name) {
		this.name = name;
	}

	public final void addArgument(OperationNode argument) {
		arguments.add(argument);
	}

	public final ArrayList<OperationNode> getArguments() {
		return arguments;
	}

	public final String getName() {
		return name;
	}

	@Override
	public final OperationNode getNext() {
		return next;
	}

	@Override
	public final void setNext(OperationNode variableOrFunction) {
		next = variableOrFunction;
	}

}
