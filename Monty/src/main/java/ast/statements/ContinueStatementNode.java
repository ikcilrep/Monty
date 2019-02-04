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

import ast.Node;
import ast.NodeTypes;
import ast.RunnableNode;
import sml.data.returning.Nothing;

public class ContinueStatementNode extends Node implements RunnableNode {

	public ContinueStatementNode(String fileName, int line) {
		this.nodeType = NodeTypes.CONTINUE_STATEMENT;
		this.fileName = fileName;
		this.line = line;
	}

	@Override
	public Object run() {
		return Nothing.continueType;
	}
}
