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

public class IOBlocks {

	public static Block readBlockFromFile(String path, String fileName, int line) {
		var tokens = Lexer.lex(FileIO.readFile(path, fileName, line), path, 1, new OptimizedTokensArray(), 0);
		var block = Parser.parse(tokens);
		block.getFunctions().put("nothing", new sml.data.returning.Nothing());
		return block;
	}

}
