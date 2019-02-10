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
				  "int counter;\n"
				+ "for x in iterable;\n"
				+ 	"if counter i ==;\n"
				+ 		"return x;\n"
				+ 		"println(\"ewqq\");\n"
				+ 	"end;\n"
				+ 	"counter 1 +=;\n"
				+ "end;\n"
				+ "logError(\"Object doesn't have \" i \" element\" + +);\n",
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
