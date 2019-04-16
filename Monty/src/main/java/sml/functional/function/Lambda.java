package sml.functional.function;

import java.util.ArrayList;

import ast.Block;
import ast.declarations.CustomFunctionDeclarationNode;
import ast.declarations.FunctionDeclarationNode;
import ast.expressions.OperationNode;
import ast.expressions.VariableNode;
import ast.statements.ReturnStatementNode;
import parser.LogError;

public class Lambda extends FunctionDeclarationNode {

	public Lambda() {
		super("lambda");
	}

	@Override
	public Function call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
		int argumentsLength = arguments.size() - 1;
		var fileName = callFileName + "lambda(" + callLine + ")";
		var function = new CustomFunctionDeclarationNode("");
		var functionExpression = arguments.get(argumentsLength);
		var block = new Block(functionExpression.getParent());
		functionExpression.setParent(block);
		functionExpression.setFileName(fileName);
		function.setBody(block);
		block.addChild(new ReturnStatementNode(functionExpression, fileName, callLine));
		for (int i = 0; i < argumentsLength; i++) {
			var argument = arguments.get(i).getOperand();
			if (!(argument instanceof VariableNode))
				new LogError("Argument " + i + " (counting from zero) should be just parameter name.", callFileName,
						callLine);
			function.addParameter(((VariableNode) argument).getName());
		}
		return new Function(function);
	}

}
