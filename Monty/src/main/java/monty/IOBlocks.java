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
import sml.functional.iterable.Filter;
import sml.functional.iterable.Map;
import sml.functional.iterable.NewIterable;
import sml.io.Input;
import sml.io.Print;
import sml.io.Println;

public class IOBlocks {
	public static FunctionDeclarationNode list;
	public static FunctionDeclarationNode iterable;
	public static FunctionDeclarationNode length;
	public static FunctionDeclarationNode logError;
	public static FunctionDeclarationNode f;
	public static FunctionDeclarationNode lambda;
	public static Block range;
	public static FunctionDeclarationNode print;
	public static FunctionDeclarationNode println;
	public static FunctionDeclarationNode input;
	public static FunctionDeclarationNode map;
	public static FunctionDeclarationNode filter;
	public static VariableDeclarationNode nothing;

	static {
		list = new sml.data.list.NewList();
		iterable = new NewIterable();
		logError = new sml.errors.LogError();
		nothing = new sml.data.returning.Nothing();
		f = new FunctionByName();
		lambda = new Lambda();
		range = Parser.parse(Lexer.lex(Sml.RANGE_CODE, "range.mt"));
		print = new Print();
		println = new Println();
		input = new Input();
		length = new sml.data.Length();
		map = new Map();
		filter = new Filter();
	}

	public static void autoImport(Block block) {
		var functions = block.getFunctions();
		block.getVariables().put("Nothing", nothing);
		block.concat(range);
		functions.put("List", list);
		functions.put("f", f);
		functions.put("lambda", lambda);
		functions.put("Iterable", iterable);
		functions.put("map", map);
		functions.put("filter", filter);
		functions.put("length", length);
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
