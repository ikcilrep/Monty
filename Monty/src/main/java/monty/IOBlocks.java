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
import lexer.Lexer;
import lexer.OptimizedTokensArray;
import parser.parsing.Parser;
import sml.io.*;
public class IOBlocks {
	private static void autoImport(Block block) {
		var functions = block.getFunctions();
		functions.put("nothing", new sml.data.returning.Nothing());
		functions.put("f", new sml.functional.function.NewFunction());
		functions.put("[A]", new sml.data.array.NewArray());
		functions.put("[L]", new sml.data.list.NewList());
		functions.put("Range", new sml.iterations.range.NewRange());
		functions.put("Iterable", new sml.functional.iterable.NewIterable());
		functions.put("length", new sml.data.Length());
		functions.put("print", new Print());
		functions.put("println", new Println());
		functions.put("input", new Input());

	}
	public static Block readBlockFromFile(String path, String fileName, int line) {
		var tokens = Lexer.lex(FileIO.readFile(path, fileName, line), path, 1, new OptimizedTokensArray(), 0);
		var block = Parser.parse(tokens);
		autoImport(block);
		return block;
	}

}
