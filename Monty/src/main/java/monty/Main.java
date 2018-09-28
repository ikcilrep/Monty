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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import ast.Block;
import lexer.LexerConfig;
import lexer.MontyToken;
import parser.MontyException;
import parser.parsing.Parser;

import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class Main {
	public static String[] argv = null;
	public static String path;

	public static void main(String[] args) throws FileNotFoundException {
		if (args.length == 0 || args[0] == "-h") {
			System.out.println("To run:\tjava -jar Monty.jar [file_name.(mt|mtc)]");
			System.out.println("To compile:\tjava -jar Monty.jar [input_file_name.mt] -o [output_file_name.mtc]");

		} else {
			argv = args;

			// path = Main.class.getResource("Examples/files.mt").getPath();
			path = args[0];

			int from = argv.length > 1 && argv[1].equals("-o") ? 3 : 1;
			var block = getBlock(from);
			if (from == 1)
				block.run();
			else {
				FileOutputStream fos = new FileOutputStream(argv[2]);
				GZIPOutputStream gos = null;
				try {
					gos = new GZIPOutputStream(fos);
				} catch (IOException e1) {
					e1.printStackTrace();
				}

				try {
					ObjectOutputStream oos = new ObjectOutputStream(gos);
					oos.writeObject(block);
					oos.flush();
					oos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static Block getBlock(int from) {
		Block block = null;
		Importing.setLibraries(from);
		if (path.endsWith(".mt")) {
			var lb = LexerConfig.getLexer(FileIO.readFile(path));

			List<MontyToken> tokens = lb.getAllTokens();
			block = Parser.parse(tokens);
			block.addFunction(new sml.data.returning.Nothing());
		} else if (path.endsWith(".mtc")) {
			FileInputStream fis = null;
			GZIPInputStream gis = null;
			try {
				fis = new FileInputStream(path);
				gis = new GZIPInputStream(fis);
			} catch (IOException e) {
				new MontyException("File not found:\t" + path);
			}

			ObjectInputStream ois;
			try {
				ois = new ObjectInputStream(gis);
				block = (Block) ois.readObject();
				ois.close();
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
				new MontyException("Wrong file format");
			}
		} else {
			new MontyException("Wrong file extensions");
		}
		return block;
	}
}
