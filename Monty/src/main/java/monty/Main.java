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

import java.io.FileNotFoundException;

import parser.parsing.Parser;

public class Main {

	public static String[] argv = null;
	public static String path;

	public static void main(String[] args) throws FileNotFoundException {
		if (args.length < 1) {
			System.out.println("To run:\tjava -jar Monty.jar [file_name.mt]");
			System.exit(1);
		}	
		
		Parser.libraries.put("sml", new sml.Sml());
		path = args[0];
		argv = args;
		IOBlocks.readBlockFromFile(path).run();
	}

}
