package sml.functional.function;

import java.util.ArrayList;

import ast.Block;
import ast.declarations.CustomFunctionDeclarationNode;
import ast.declarations.FunctionDeclarationNode;
import ast.expressions.OperationNode;
import ast.statements.ReturnStatementNode;
import lexer.Lexer;
import parser.DataTypes;
import parser.parsing.AdderToBlock;
import parser.parsing.ExpressionParser;

public class NewFunction extends FunctionDeclarationNode{

	public NewFunction() {
		super("f", DataTypes.ANY);
		setBody(new Block(null));
		addParameter("str", DataTypes.STRING);
	}

	@Override
	public Object call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
		setArguments(arguments, callFileName, callLine);
		var str = (String)getBody().getVariable("str").getValue();
		var parent = arguments.get(0).getParent();
		if (!str.startsWith("/"))
			return new Function(parent.getFunction(str));
		var functionDeclaration = new CustomFunctionDeclarationNode(callFileName, DataTypes.ANY);
		var array = str.substring(1).split("->");
		var fileName = callFileName+":lambda("+callLine+")";
		AdderToBlock.parseFunctionsParameters(0, Lexer.lex(array[0], fileName), functionDeclaration);
		var body = new Block(parent);
		body.addChild(new ReturnStatementNode(ExpressionParser.parse(body, Lexer.lex(array[1], fileName)), fileName, callLine));
		functionDeclaration.setBody(body);
		return new Function(functionDeclaration);
	}

}
