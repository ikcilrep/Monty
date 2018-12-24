package monty;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import ast.Block;
import lexer.LexerConfig;
import lexer.Token;
import parser.LogError;
import parser.parsing.Parser;

public class IOBlocks {

	public static Block readBlock(int from, String path) {
		Importing.setLibraries(from);
		if (path.endsWith(".mt"))
			return readBlockFromFile(path);
		else if (path.endsWith(".mtc"))
			return readCompiledBlockFromFile(path);
		new LogError("File with wrong extension: " + path);
		return null;
	}

	private static Block readBlockFromFile(String path) {
		var lb = LexerConfig.getLexer(FileIO.readFile(path), path);
		List<Token> tokens = lb.getAllTokens();
		var block = Parser.parse(tokens);
		block.getFunctions().put("nothing", new sml.data.returning.Nothing());
		return block;
	}
	
	public static Block readCompiledBlockFromFile(String path) {
		return readBlockFromGZIP(readGZIPBlock(path), path);
	}

	private static GZIPInputStream readGZIPBlock(String path) {
		FileInputStream fis = null;
		GZIPInputStream gis = null;
		try {
			fis = new FileInputStream(path);
			gis = new GZIPInputStream(fis);
		} catch (IOException e) {
			new LogError("File not found:\t" + path);
		}
		return gis;
	}

	private static Block readBlockFromGZIP(GZIPInputStream gis, String path) {
		ObjectInputStream ois;
		Block block = null;
		try {
			ois = new ObjectInputStream(gis);
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

	public static void compileAndWriteBlock(Block block, String outputPath) {
		try {
			FileOutputStream fos = new FileOutputStream(outputPath);
			GZIPOutputStream gos = new GZIPOutputStream(fos);
			ObjectOutputStream oos = new ObjectOutputStream(gos);
			oos.writeObject(block);
			oos.flush();
			oos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
