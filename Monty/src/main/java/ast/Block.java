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

package ast;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import ast.declarations.FunctionDeclarationNode;
import ast.declarations.StructDeclarationNode;
import ast.declarations.VariableDeclarationNode;
import ast.expressions.OperationNode;
import ast.statements.ChangeToStatementNode;
import ast.statements.ContinueStatementNode;
import ast.statements.DoWhileStatementNode;
import ast.statements.ForStatementNode;
import ast.statements.IfStatementNode;
import ast.statements.ReturnStatementNode;
import ast.statements.ThreadStatement;
import ast.statements.WhileStatementNode;
import lexer.Token;
import parser.DataTypes;
import parser.LogError;
import sml.casts.ToArray;
import sml.casts.ToBoolean;
import sml.casts.ToFloat;
import sml.casts.ToInt;
import sml.casts.ToList;
import sml.casts.ToStack;
import sml.casts.ToString;
import sml.data.returning.BreakType;
import sml.data.returning.Nothing;
import sml.threading.MontyThread;

public class Block extends Node implements Serializable,Cloneable {
	private static final long serialVersionUID = -1974629623424063560L;
	private LinkedList<Node> children = new LinkedList<>();
	protected HashMap<String, FunctionDeclarationNode> functions = new HashMap<>();
	private Block parent;
	private HashMap<String, VariableDeclarationNode> variables = new HashMap<>();

	public Block(Block parent) {
		this.parent = parent;
	}

	public Block(Block parent, NodeTypes nodeType) {
		this.parent = parent;
		super.nodeType = nodeType;
	}

	public void addChild(Node child) {
		children.add(child);
	}

	public void addFunction(FunctionDeclarationNode function) {
		String name = function.getName();
		if (functions.containsKey(name))
			new LogError("Function " + name + " already exists");
		functions.put(name, function);
	}

	public void addFunction(FunctionDeclarationNode function, String fileName, int line) {
		String name = function.getName();
		function.setFileName(fileName);
		function.setLine(line);
		if (functions.containsKey(name)) {
			var existing_function = functions.get(name);
			int[] lines = { existing_function.getLine(), function.getLine() };
			String[] fileNames = { existing_function.getFileName(), function.getFileName() };
			new LogError("Function " + name + " already exists", fileNames, lines);
		}
		functions.put(name, function);
	}

	public void addFunction(FunctionDeclarationNode function, Token token) {
		String name = function.getName();
		function.setFileName(token.getFileName());
		function.setLine(token.getLine());
		if (functions.containsKey(name)) {
			var existing_function = functions.get(name);
			int[] lines = { existing_function.getLine(), function.getLine() };
			String[] fileNames = { existing_function.getFileName(), function.getFileName() };
			new LogError("Function " + name + " already exists", fileNames, lines);
		}
		functions.put(name, function);
	}

	
	public void addVariable(VariableDeclarationNode variable) {
		String name = variable.getName();
		if (variables.containsKey(name))
			new LogError("Variable " + name + " already exists");
		variables.put(name, variable);
	}

	public void setParent(Block parent) {
		this.parent = parent;
	}

	public void addVariable(VariableDeclarationNode variable, String fileName, int line) {
		String name = variable.getName();
		variable.setFileName(fileName);
		variable.setLine(line);
		if (variables.containsKey(name)) {
			var existing_variable = variables.get(name);
			int[] lines = { existing_variable.getLine(), variable.getLine() };
			String[] fileNames = { existing_variable.getFileName(), variable.getFileName() };
			new LogError("Variable " + name + " already exists", fileNames, lines);
		}
		variables.put(name, variable);
	}

	public void addVariable(VariableDeclarationNode variable, Token token) {
		String name = variable.getName();
		variable.setFileName(token.getFileName());
		variable.setLine(token.getLine());
		if (variables.containsKey(name)) {
			var existing_variable = variables.get(name);
			int[] lines = { existing_variable.getLine(), variable.getLine() };
			String[] fileNames = { existing_variable.getFileName(), variable.getFileName() };
			new LogError("Variable " + name + " already exists", fileNames, lines);
		}
		variables.put(name, variable);
	}

	public void concat(Block block) {
		block.run();
		var variablesSet = block.getVariables().entrySet();
		for (Map.Entry<String, VariableDeclarationNode> entry : variablesSet) {
			addVariable(entry.getValue());
		}

		var functionsSet = block.getFunctions().entrySet();
		for (Map.Entry<String, FunctionDeclarationNode> entry : functionsSet) {
			addFunction(entry.getValue());
		}

		var children = block.getChildren();
		for (Node child : children) {
			addChild(child);
		}
	}

	public boolean doesContainFunction(String name) {
		return functions.containsKey(name);
	}

	public boolean doesContainVariable(String name) {
		return variables.containsKey(name);
	}
	
	public LinkedList<Node> getChildren() {
		return children;
	}

	public FunctionDeclarationNode getFunctionByName(String name) {
		Block block = this;
		while (!block.functions.containsKey(name)) {
			var parent = block.getParent();
			if (parent == null)
				new LogError("There isn't function with name:\t" + name);
			block = parent;
		}
		return block.functions.get(name);

	}

	public FunctionDeclarationNode getFunctionByName(String name, String fileName, int line) {
		Block block = this;
		while (!block.functions.containsKey(name)) {
			var parent = block.getParent();
			if (parent == null)
				new LogError("There isn't function with name:\t" + name, fileName, line);
			block = parent;
		}
		return block.functions.get(name);

	}
	
	public HashMap<String, FunctionDeclarationNode> getFunctions() {
		return functions;
	}

	public Block getParent() {
		return parent;
	}

	public VariableDeclarationNode getVariableByName(String name) {
		Block block = this;
		while (!block.variables.containsKey(name)) {
			var parent = block.getParent();
			if (parent == null)
				new LogError("There isn't variable with name:\t" + name);
			block = parent;
		}
		return block.variables.get(name);
	}

	public VariableDeclarationNode getVariableByName(String name, String fileName, int line) {
		Block block = this;
		while (!block.variables.containsKey(name)) {
			var parent = block.getParent();
			if (parent == null)
				new LogError("There isn't variable with name:\t" + name, fileName, line);
			block = parent;
		}
		return block.variables.get(name);
	}

	public HashMap<String, VariableDeclarationNode> getVariables() {
		return variables;
	}

	public Object run() {
		for (Node child : children) {
			switch (child.getNodeType()) {
			case OPERATION:
				var childCastedToVariable = ((OperationNode) child);
				childCastedToVariable.run();
				break;
			case STRUCT_DECLARATION:
				var childCastedToStruct = ((StructDeclarationNode) child);
				childCastedToStruct.run();
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
				loop: while ((boolean) childCastedToWhileStatement.getCondition().run()) {
					var body = childCastedToWhileStatement.getBody();
					var result = body.run();
					if (result instanceof BreakType)
						break loop;
					if (result instanceof ContinueStatementNode)
						continue;
					if (result != null)
						return result;
				}
				break;
			case DO_WHILE_STATEMENT:
				var childCastedToDoWhileStatement = ((DoWhileStatementNode) child);
				loop: do {
					var body = childCastedToDoWhileStatement.getBody();
					var result = body.run();
					if (result instanceof BreakType)
						break loop;
					if (result instanceof ContinueStatementNode)
						continue;
					if (result != null)
						return result;
				} while ((boolean) childCastedToDoWhileStatement.getCondition().run());
				break;
			case FOR_STATEMENT:
				var childCastedToForStatement = ((ForStatementNode) child);
				var name = childCastedToForStatement.getVariableName();
				var toIter = childCastedToForStatement.getArray().run();
				if (!(toIter instanceof Iterable<?>))
					new LogError("Can't iterate over:\t" + toIter.getClass().getName(), child.getFileName(),
							child.getLine());
				var iterable = (Iterable<?>) toIter;
				loop: for (Object e : iterable) {
					var body = childCastedToForStatement.getBody();
					if (body.doesContainVariable(name))
						body.getVariableByName(name, body.getFileName(), body.getLine()).setValue(e);
					else {
						body.addVariable(new VariableDeclarationNode(name, DataTypes.ANY));
						body.getVariableByName(name, body.getFileName(), body.getLine()).setValue(e);
					}
					var result = body.run();
					if (result instanceof BreakType)
						break loop;
					if (result instanceof ContinueStatementNode)
						continue;
					if (result != null)
						return result;
				}
				break;
			case CHANGE_TO_STATEMENT:
				var childCastedToChangeToStatement = ((ChangeToStatementNode) child);
				var newVariableType = childCastedToChangeToStatement.getDataType();
				var variable = getVariableByName(childCastedToChangeToStatement.getToChangeType().getName(),
						child.getFileName(), child.getLine());
				if (!variable.isDynamic()) {
					int[] lines = { child.getLine(), variable.getLine() };
					String[] fileNames = { child.getFileName(), variable.getFileName() };
					new LogError("Can't change type of static variable:\tchange " + variable.getName() + " to "
							+ newVariableType.toString().toLowerCase(), fileNames, lines);
				}

				variable.setType(newVariableType);
				switch (newVariableType) {
				case INTEGER:
					variable.setValue(new ToInt().toInt(variable.getValue(), child.getFileName(), child.getLine()));
					break;
				case BOOLEAN:
					variable.setValue(
							new ToBoolean().toBoolean(variable.getValue(), child.getFileName(), child.getLine()));
					break;
				case FLOAT:
					variable.setValue(new ToFloat().toFloat(variable.getValue(), child.getFileName(), child.getLine()));
					break;
				case STRING:
					variable.setValue(
							new ToString().toString(variable.getValue(), child.getFileName(), child.getLine()));
					break;
				case ARRAY:
					variable.setValue(ToArray.toArray(variable.getValue(), child.getFileName(), child.getLine()));
					break;
				case LIST:
					variable.setValue(ToList.toList(variable.getValue(), child.getFileName(), child.getLine()));
					break;
				case STACK:
					variable.setValue(ToStack.toStack(variable.getValue(), child.getFileName(), child.getLine()));
					break;
				default:
					break;

				}
				break;
			case BREAK_STATEMENT:
				return Nothing.breakType;
			case CONTINUE_STATEMENT:
				return Nothing.continueType;
			case RETURN_STATEMENT:
				return ((ReturnStatementNode) child).getExpression().run();
			case THREAD_STATEMENT:
				new MontyThread(((ThreadStatement) child).getExpression());
			default:
				break;
			}
		}
		return null;
	}

	public void setVariables(HashMap<String, VariableDeclarationNode> variables) {
		this.variables = variables;
	}
	
	public Block copy() {
		try {
			return (Block) clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}
}
