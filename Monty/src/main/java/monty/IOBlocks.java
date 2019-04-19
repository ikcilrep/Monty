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
package monty;

import ast.Block;
import ast.declarations.FunctionDeclarationNode;
import ast.declarations.VariableDeclarationNode;
import lexer.Lexer;
import lexer.OptimizedTokensArray;
import parser.parsing.Parser;
import sml.Sml;
import sml.functional.function.FunctionByName;
import sml.functional.function.Lambda;
import sml.io.Input;
import sml.io.Print;
import sml.io.Println;

public class IOBlocks {
	public static FunctionDeclarationNode list;
	public static FunctionDeclarationNode logError;
	public static FunctionDeclarationNode f;
	public static FunctionDeclarationNode lambda;
	public static FunctionDeclarationNode print;
	public static FunctionDeclarationNode println;
	public static FunctionDeclarationNode input;
	public static VariableDeclarationNode nothing;
	public static Block[] writtenInMonty;

	static {
		list = new sml.data.list.NewList();
		logError = new sml.errors.LogError();
		nothing = new sml.data.returning.Nothing();
		f = new FunctionByName();
		lambda = new Lambda();
		writtenInMonty = new Block[Sml.numberOfFiles];
		int i = 0;
		for (var code : Sml.code)
			writtenInMonty[i] = Parser.parse(Lexer.lex(code, Sml.paths[i++]));
		
		print = new Print();
		println = new Println();
		input = new Input();
	}

	public static void autoImport(Block block) {
		var functions = block.getFunctions();
		block.getVariables().put("Nothing", nothing);
		for (var parsed : writtenInMonty)
			block.concat(parsed);
		functions.put("List", list);
		functions.put("f", f);
		functions.put("lambda", lambda);
		functions.put("print", print);
		functions.put("println", println);
		functions.put("input", input);

	}

	public static Block readBlockFromFile(String path, String fileName, int line) {
		var block = Parser
				.parse(Lexer.lex(FileIO.readFile(path, fileName, line), path, 1, new OptimizedTokensArray(), 0));
		autoImport(block);
		return block;
	}

}
