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

import ast.Block;
import parser.parsing.Parser;

public class Main {

	public static String[] argv = null;
	public static String path;

	public static void main(String[] args) throws FileNotFoundException {
		boolean isItToRun = false;
		if ((args.length < 2 || !(isItToRun = args[0].equals("-r"))) && (args.length < 3 || !args[0].equals("-c"))) {
			System.out.println((args.length < 2 || !(isItToRun = args[0].equals("-r"))));
			System.out.println((args.length < 3 || !args[0].equals("-c")));
			System.out.println(
					"To run:\tjava -jar Monty.jar [file_name.(mt|mtc)]\nTo compile:\\tjava -jar Monty.jar [input_file_name.mt] -o [output_file_name.mtc]");
			System.exit(0);
		}
		Parser.libraries.put("sml", new sml.Sml());
		path = args[1];
		argv = args;
		Block block = null;
		block = IOBlocks.readBlock(args[1]);
		if (isItToRun) {
			block.run();
		} else {
			IOBlocks.compileAndWriteBlock(block, args[2]);
		}
	}

}
