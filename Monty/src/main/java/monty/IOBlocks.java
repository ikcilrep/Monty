package monty;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import ast.Block;
import lexer.Lexer;
import parser.LogError;
import parser.parsing.Parser;
import sml.data.array.Array;

public class IOBlocks {

	public static void compileAndWriteBlock(Block block, String outputPath) {
		try {
			FileOutputStream fos = new FileOutputStream(outputPath);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(block);
			oos.flush();
			oos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static Block readBlock(String path) {
		if (path.endsWith(".mt"))
			return readBlockFromFile(path);
		else if (path.endsWith(".mtc"))
			return readCompiledBlockFromFile(path);
		new LogError("File with wrong extension: " + path);
		return null;
	}

	private static Block readBlockFromFile(String path) {
		var tokens = Lexer.lex(FileIO.readFile(path), path, 1, new Array<>(), 0);
		var block = Parser.parse(tokens);
		block.getFunctions().put("nothing", new sml.data.returning.Nothing());
		return block;
	}

	private static Block readCompiledBlock(FileInputStream fis, String path) {
		ObjectInputStream ois;
		Block block = null;
		try {
			ois = new ObjectInputStream(fis);
			var obj = ois.readObject();
			if (!(obj instanceof Block))
				new LogError("This isn't file with program or library:\t" + path);
			block = (Block) obj;
			ois.close();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
			new LogError("Wrong file format");
		}
		return block;
	}

	public static Block readCompiledBlockFromFile(String path) {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(path);
		} catch (IOException e) {
			new LogError("File not found:\t" + path);
		}
		return readCompiledBlock(fis, path);
	}

}
