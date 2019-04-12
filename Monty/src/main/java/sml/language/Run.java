package sml.language;

import java.util.ArrayList;

import ast.Block;
import ast.declarations.FunctionDeclarationNode;
import ast.expressions.OperationNode;
import lexer.Lexer;
import parser.DataTypes;
import parser.parsing.Parser;
import sml.data.returning.Nothing;

public final class Run extends FunctionDeclarationNode {

	public Run() {
		super("run", DataTypes.VOID);
		addParameter("code", DataTypes.STRING);
		setBody(new Block(null));
	}

	@Override
	public Object call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
		setArguments(arguments, callFileName, callLine);
		Parser.parse(Lexer.lex((String) getBody().getVariable("code").getValue(), callFileName, callLine)).run();
		return Nothing.nothing;
	}

}
