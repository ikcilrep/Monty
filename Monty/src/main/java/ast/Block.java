package ast;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ast.declarations.FunctionDeclarationNode;
import ast.declarations.VariableDeclarationNode;
import ast.expressions.FunctionCallNode;
import ast.expressions.OperationNode;
import ast.statements.ChangeToStatementNode;
import ast.statements.DoWhileStatementNode;
import ast.statements.IfStatementNode;
import ast.statements.ThreadStatement;
import ast.statements.ReturnStatementNode;
import ast.statements.WhileStatementNode;
import parser.MontyException;
import sml.casts.*;
import sml.threading.MontyThread;

public class Block extends Node {
	private final HashMap<String, Block> blocks = new HashMap<>();
	private ArrayList<Node> children = new ArrayList<>();
	private final HashMap<String, FunctionDeclarationNode> functions = new HashMap<>();
	private String name = "";
	private Block parent;
	private HashMap<String, VariableDeclarationNode> variables = new HashMap<>();

	public Block(Block parent) {
		this.parent = parent;
	}

	public void addBlock(Block block) {
		String name = block.getName();
		if (blocks.containsKey(name))
			new MontyException("Block with this label " + name + " already exists");
		blocks.put(name, block);
	}

	public void addChild(Node child) {
		children.add(child);
	}

	public void addFunction(FunctionDeclarationNode function) {
		String name = function.getName();
		if (variables.containsKey(name))
			new MontyException("Function " + name + " already exists");
		functions.put(name, function);
	}

	public void addVariable(VariableDeclarationNode variable) {
		String name = variable.getName();
		if (variables.containsKey(name)) {
			new MontyException("Variable " + name + " already exists");
		}
		variables.put(name, variable);
	}

	public void concat(Block block) {
		var variablesSet = block.getVariables().entrySet();
		for (Map.Entry<String, VariableDeclarationNode> entry : variablesSet) {
			addVariable((VariableDeclarationNode) entry.getValue());
		}

		var functionsSet = block.getFunctions().entrySet();
		for (Map.Entry<String, FunctionDeclarationNode> entry : functionsSet) {
			addFunction(((FunctionDeclarationNode) entry.getValue()));
		}

		var children = block.getChildren();
		for (Node child : children) {
			addChild(child);
		}
	}

	public boolean doesContainVariable(String name) {
		return variables.containsKey(name);
	}

	public Block getBlockByName(String name) {
		Block block = this;
		while (true) {
			if (block.blocks.containsKey(name))
				return block.blocks.get(name);
			var parent = block.getParent();
			if (parent == null) {
				new MontyException("There isn't block with name:\t" + name);
			}
			block = parent;
		}

	}

	public ArrayList<Node> getChildren() {
		return children;
	}

	public FunctionDeclarationNode getFunctionByName(String name) {
		Block block = this;
		while (true) {
			if (block.functions.containsKey(name))
				return block.functions.get(name);
			var parent = block.getParent();
			if (parent == null) {
				new MontyException("There isn't function with name:\t" + name);
			}
			block = parent;
		}
	}

	private HashMap<String, FunctionDeclarationNode> getFunctions() {
		return functions;
	}

	public String getName() {
		return name;
	}

	public Block getParent() {
		return parent;
	}

	public VariableDeclarationNode getVariableByName(String name) {
		Block block = this;
		while (true) {
			if (block.variables.containsKey(name))
				return block.variables.get(name);
			var parent = block.getParent();
			if (parent == null)
				new MontyException("There isn't variable with name:\t" + name);
			block = parent;
		}
	}

	public HashMap<String, VariableDeclarationNode> getVariables() {
		return variables;
	}

	public Object run() {
		for (Node child : children) {
			switch (child.getNodeType()) {
			case OPERATION:
				var childCastedToVariable = ((OperationNode) child);
				if (childCastedToVariable.getOperand().equals(NodeTypes.FUNCTION_CALL)) {
					var functionToCall = ((FunctionCallNode) child);
					var function = getFunctionByName(functionToCall.getName());
					function.call(functionToCall.getArguments());
				} else if (!(childCastedToVariable.getRightOperand() != null
						&& childCastedToVariable.getLeftOperand().getNodeType().equals(NodeTypes.VARIABLE)
						&& childCastedToVariable.getRightOperand().getOperand().toString().contains("="))) {
					childCastedToVariable.run();
				} else
					new MontyException("Some expression hasn't got any sense!!!");

				break;
			case IF_STATEMENT:
				var childCastedToIfStatement = ((IfStatementNode) child);
				var elseBody = childCastedToIfStatement.getElseBody();
				if ((boolean) childCastedToIfStatement.getCondition().run()) {
					var result = childCastedToIfStatement.run();
					if (result != null)
						return result;
				} else if (elseBody != null) {
					var result = elseBody.run();
					if (result != null)
						return result;
				}
				break;
			case WHILE_STATEMENT:
				var childCastedToWhileStatement = ((WhileStatementNode) child);
				while ((boolean) childCastedToWhileStatement.getCondition().run()) {
					var body = childCastedToWhileStatement.getBody();
					var result = body.run();
					if (result != null)
						return result;
				}
				break;
			case DO_WHILE_STATEMENT:
				var childCastedToDoWhileStatement = ((DoWhileStatementNode) child);
				do {
					var body = childCastedToDoWhileStatement.getBody();
					var result = body.run();	
					if (result != null)
						return result;
				} while ((boolean) childCastedToDoWhileStatement.getCondition().run());
				break;
			case CHANGE_TO_STATEMENT:
				var childCastedToChangeToStatement = ((ChangeToStatementNode) child);
				var newVariableType = childCastedToChangeToStatement.getDataType();
				var variable = getVariableByName(childCastedToChangeToStatement.getToChangeType().getName());
				if (!variable.isDynamic())
					new MontyException("Can't change type of static variable:\tchange " + variable.getName() + " to "
							+ newVariableType.toString().toLowerCase());

				variable.setType(newVariableType);
				switch (newVariableType) {
				case INTEGER:
					variable.setValue(ToInt.toInt(variable.getValue()));
					break;
				case BOOLEAN:
					variable.setValue(ToBoolean.toBoolean(variable.getValue()));
					break;
				case FLOAT:
					variable.setValue(ToFloat.toFloat(variable.getValue()));
					break;
				case STRING:
					variable.setValue(ToString.toString(variable.getValue()));
					break;
				case ARRAY:
					variable.setValue(ToArray.toArray(variable.getValue()));
					break;
				default:
					break;

				}
				break;
			case RETURN_STATEMENT:
				return ((ReturnStatementNode) child).getExpression().run();
			case THREAD_STATEMENT:
				new MontyThread(((ThreadStatement) child).getExpression());
				break;
			default:
				break;
			}
		}
		return null;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setVariables(HashMap<String, VariableDeclarationNode> variables) {
		this.variables = variables;
	}
}
