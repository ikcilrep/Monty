package monty;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import parser.MontyException;

public abstract class FileIO {
	public static String readFile(String path) {
		BufferedReader br = null;
		var text = new StringBuilder();
		try {
			var fr = (FileReader) null;
			try {
				fr = new FileReader(path);
			} catch (FileNotFoundException e) {
				new MontyException("This file isn't exists:\t" + path);
			}
			br = new BufferedReader(fr);
			String line;
			while ((line = br.readLine()) != null) {
				text.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null) {
					br.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return text.toString();
	}
}
