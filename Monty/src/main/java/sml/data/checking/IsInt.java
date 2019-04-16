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

package sml.data.checking;

import java.math.BigInteger;
import java.util.ArrayList;

import ast.Block;
import ast.declarations.FunctionDeclarationNode;
import ast.expressions.OperationNode;

public final class IsInt extends FunctionDeclarationNode {

	public IsInt() {
		super("isInt");
		setBody(new Block(null));
		addParameter("toCheck");
	}

	@Override
	public Object call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
		setArguments(arguments, callFileName, callLine);
		return getBody().getVariableValue("toCheck") instanceof BigInteger;
	}

}
