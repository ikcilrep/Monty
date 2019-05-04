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
package parser;

import lexer.Token;

public class LogError {
    public LogError(String message) {
        System.out.println(getMessage(message));
        System.exit(0);
    }

    public LogError(String message, String fileName, int line) {
        System.out.println(getMessage(message) + "\nLook at " + line + " line in " + fileName + ".");
        System.exit(0);
    }

    public LogError(String message, String[] fileNames, int[] lines) {
        if (fileNames.length != lines.length)
            new LogError("Language creator mess something up!");
        var places = new StringBuilder();
        for (int i = 0; i < lines.length; i++) {
            places.append(lines[i]);
            places.append(" line in ");
            places.append(fileNames[i]);

            if (i + 1 < lines.length)
                places.append(", ");
        }
        System.out.println(getMessage(message) + "\nLook at " + places + ".");
        System.exit(0);
    }

    public LogError(String message, Token token) {
        System.out.println(
                getMessage(message) + "\nLook at " + token.getLine() + " line in " + token.getFileName() + ".");
        System.exit(0);
    }

    private static String getMessage(String message) {
        var lastChar = message.charAt(message.length() - 1);
        if (Character.isAlphabetic(lastChar) || Character.isDigit(lastChar))
            return message + ".";
        return message;
    }
}
