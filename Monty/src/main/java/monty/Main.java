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

public class Main {
	
	public static String[] argv = null;
	public static String path;
	public static String standard_library_name = "sml";
	public static void main(String[] args) throws FileNotFoundException {
		if (args.length == 0 || args[0] == "-h")
			System.out.println("To run:\tjava -jar Monty.jar [file_name.(mt|mtc)]\nTo compile:\\tjava -jar Monty.jar [input_file_name.mt] -o [output_file_name.mtc]");
		else {
			argv = args;
			path = args[0];
			int from = argv.length > 1 && argv[1].equals("-o") ? 3 : 1;
			var block = IOBlocks.readBlock(from, path);
			if (from == 1)
				block.run();
			else 
				IOBlocks.compileAndWriteBlock(block, argv[2]);
		}
	}

}
