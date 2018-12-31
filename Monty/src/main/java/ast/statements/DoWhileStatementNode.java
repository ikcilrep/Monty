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

package ast.statements;

import ast.Block;
import ast.NodeTypes;
import ast.expressions.OperationNode;

public class DoWhileStatementNode extends StatementNode {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2423018612401482216L;
	private OperationNode condition;
	private Block body;

	public DoWhileStatementNode(OperationNode condition, String fileName, int line) {
		this.condition = condition;
		super.nodeType = NodeTypes.DO_WHILE_STATEMENT;
		this.fileName = fileName;
		this.line = line;
	}

	public Block getBody() {
		return body;
	}

	public OperationNode getCondition() {
		return condition;
	}

	public void setBody(Block body) {
		this.body = body;
	}

}
