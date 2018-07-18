package monty;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public abstract class FileIO {
	public static String readFile(String path) {
		BufferedReader br = null;
		var text = new StringBuilder();
		try {
			br = new BufferedReader(new FileReader(path));
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
