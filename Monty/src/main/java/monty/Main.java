/*
Copyright 2018 Szymon Perlicki

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

import java.io.FileNotFoundException;
import java.util.List;
import lexer.LexerConfig;
import lexer.MontyToken;
import parser.parsing.Parser;

public class Main {
	public static String[] argv = null;
	public static String path;

	public static void main(String[] args) throws FileNotFoundException {
		argv = args;
		//path = Main.class.getResource("Examples/xorEncryption.mt").getPath();
		path = args[0];
		var lb = LexerConfig.getLexer(FileIO.readFile(path));

		List<MontyToken> tokens = lb.getAllTokens();
		Importing.setLibraries();
		var block = Parser.parse(tokens);
		block.addFunction(new sml.data.returning.Nothing());
		block.run();
	}
}
