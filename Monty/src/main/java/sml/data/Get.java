package sml.data;

import java.util.ArrayList;

import ast.declarations.FunctionDeclarationNode;
import ast.expressions.OperationNode;
import lexer.Lexer;
import parser.DataTypes;
import parser.parsing.Parser;
import sml.errors.LogError;

public final class Get extends FunctionDeclarationNode {

	public Get() {
		super("get", DataTypes.ANY);
		setBody(Parser.parse(Lexer.lex(
				"int counter;"
				+ "for x in iterable;"
				+ 	"if counter i ==;"
				+ 		"return x;"
				+ 		"println(\"ewqq\");"
				+ 	"end;"
				+ 	"counter 1 +=;"
				+ "end;"
				+ "logError(\"Index \" i \" is too big for \" length(iterable) \" length\"+ + + +);",
				"Get.java")));
		getBody().addFunction(new LogError());
		getBody().addFunction(new Length());
		addParameter("i", DataTypes.INTEGER);
		addParameter("iterable", DataTypes.ANY);
	}

	@Override
	public Object call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
		setArguments(arguments, callFileName, callLine);
		return getBody().run();
	}

}
