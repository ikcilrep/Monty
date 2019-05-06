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

import parser.LogError;

import java.io.*;

public abstract class FileIO {
    private static String readFile(BufferedReader br, String path, String callFileName, int callLine) {
        var text = new StringBuilder();
        try {
            String line;
            while ((line = br.readLine()) != null)
                text.append(line).append("\n");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null)
                    br.close();
            } catch (IOException ex) {
                new LogError("Failed to read file:\t" + path, callFileName, callLine);
            }
        }
        return text.toString();
    }

    public static String readFile(InputStream in, String path) {
        return readFile(new BufferedReader(new InputStreamReader(in)), path, "Sml.java", -1);
    }

    static String readFile(String path, String callFileName, int callLine) {
        BufferedReader br;
        FileReader fr = null;
        try {
            fr = new FileReader(path);
        } catch (FileNotFoundException e) {
            new LogError("This file doesn't exist:\t" + path, callFileName, callLine);
        }

        br = new BufferedReader(fr);

        return readFile(br, path, callFileName, callLine);
    }

}
