package sml.language;

import java.util.ArrayList;

import ast.Block;
import ast.declarations.FunctionDeclarationNode;
import ast.expressions.OperationNode;
import lexer.Lexer;
import parser.parsing.Parser;
import sml.data.returning.Nothing;
import sml.data.returning.VoidType;

public final class Run extends FunctionDeclarationNode {

	public Run() {
		super("run");
		addParameter("code");
		setBody(new Block(null));
	}

	@Override
	public VoidType call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
		setArguments(arguments, callFileName, callLine);
		Parser.parse(Lexer.lex(getBody().getStringVariableValue("code", callFileName, callLine), callFileName, callLine)).run();
		return Nothing.nothing;
	}

}
