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

import ast.Block;
import ast.declarations.Constructor;
import ast.declarations.FunctionDeclarationNode;
import ast.declarations.VariableDeclarationNode;
import lexer.Lexer;
import lexer.Token;
import parser.LogError;
import parser.Tokens;
import parser.parsing.Parser;
import sml.Sml;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class Importing {
    private final static Path fileAbsolutePath = Paths.get(Main.path).getParent();
    private final static String mainPath = new File(Main.path).isAbsolute() ? "" : Paths.get("").toAbsolutePath().toString();
    private final static String mainFileLocation = mainPath
            + (mainPath.endsWith(File.separator) || mainPath.isEmpty() ? "" : File.separator)
            + (fileAbsolutePath == null ? "" : fileAbsolutePath) + File.separator;


    @SuppressWarnings("unchecked")
    private static void importAllElementsFromSmlChildren(Block block, HashMap<String, Object> addFrom, Token token) {
        for (Object value : addFrom.values())
            if (value instanceof FunctionDeclarationNode)
                block.addFunction((FunctionDeclarationNode) value, token);
            else if (value instanceof VariableDeclarationNode)
                block.addVariable((VariableDeclarationNode) value, token);
            else
                importAllElementsFromSmlChildren(block, (HashMap<String, Object>) value, token);
    }

    @SuppressWarnings("unchecked")
    private static void importElementFromSml(Block block, String[] split, String path,
                                             Token token) {
        var children = Sml.getChildren();
        Object functionVariableOrSubLibrary;

        for (int i = 1; i < split.length; i++) {
            var toImport = split[i];
            if (!children.containsKey(toImport))
                new LogError("There isn't file to import:\t" + path, token);
            else if ((functionVariableOrSubLibrary = children.get(toImport)) instanceof FunctionDeclarationNode) {
                block.addFunction((FunctionDeclarationNode) functionVariableOrSubLibrary, token);
                break;
            } else if (functionVariableOrSubLibrary instanceof VariableDeclarationNode) {
                block.addVariable((VariableDeclarationNode) functionVariableOrSubLibrary, token);
                break;
            } else if (i + 1 >= split.length)
                importAllElementsFromSmlChildren(block, (HashMap<String, Object>) functionVariableOrSubLibrary,
                        token);
            else
                children = (HashMap<String, Object>) functionVariableOrSubLibrary;
        }
    }

    public static void importFile(Block block, ArrayList<Token> tokensBeforeSemicolon) {
        var partOfPath = Tokens.getText(tokensBeforeSemicolon.subList(1, tokensBeforeSemicolon.size()));
        var path = mainFileLocation + partOfPath.replace('.', File.separatorChar);
        var file = new File(path + ".mt");
        var parent_file = new File(file.getParent() + ".mt");
        var directory = new File(path);
        var fileName = tokensBeforeSemicolon.get(1).getFileName();
        var line = tokensBeforeSemicolon.get(1).getLine();
        if (directory.exists() && directory.isDirectory())
            importFilesFromDirectory(block, directory, fileName, line);
        else if (file.exists() && file.isFile())
            importWholeFile(block, file.getPath(), fileName, line);
        else if (parent_file.exists() && parent_file.isFile()) {
            importSpecifiedElementFromBlock(block,
                    Parser.parse(Lexer.lex(FileIO.readFile(parent_file.getAbsolutePath(), fileName, line),
                            parent_file.getName(), 1, new ArrayList<>(), 0)),
                    parent_file.getPath(), file.getName().substring(0, file.getName().length() - 3), fileName, line);
        } else {
            var split = partOfPath.split("\\.");
            if (!split[0].equals("sml"))
                new LogError("There isn't file to import:\t" + path, tokensBeforeSemicolon.get(1));
            importElementFromSml(block, split, path, tokensBeforeSemicolon.get(1));
        }
    }

    private static void importFilesFromDirectory(Block block, File directory, String fileName, int line) {
        for (File fileEntry : Objects.requireNonNull(directory.listFiles())) {
            if (fileEntry.isDirectory())
                importFilesFromDirectory(block, fileEntry, fileName, line);
            else if (fileEntry.getName().endsWith(".mt"))
                importWholeFile(block, fileEntry.getAbsolutePath(), fileName, line);
            else
                new LogError(
                        "File with wrong extension: " + directory.getPath() + File.separator + fileEntry.getName());
        }
    }

    private static void importSpecifiedElementFromBlock(Block block, Block importedBlock, String path, String name, String fileName, int line) {
        var doesContainVariable = importedBlock.hasVariable(name);
        var doesContainFunction = importedBlock.hasFunction(name);
        if (doesContainVariable || doesContainFunction) {
            importedBlock.run();
            if (doesContainVariable) {
                var variable = importedBlock.getVariable(name, fileName, line);
                block.addVariable(variable, variable.getFileName(), variable.getLine());
            }
            if (doesContainFunction) {
                var function = importedBlock.getFunction(name, fileName, line);
                if (Character.isUpperCase(name.charAt(0))) {
                    var struct = importedBlock.getStructure(name);
                    block.addStruct(struct, (Constructor) function, struct.getFileName(), struct.getLine());
                } else
                    block.addFunction(function, function.getFileName(), function.getLine());

            }
        } else {
            new LogError("There aren't any function or variable with this name to import:\t" + name
                    + ". Look at this file:\t" + path);
        }
    }

    private static void importWholeFile(Block block, String path, String fileName, int line) {
        var file = new File(path);
        block.concat(Parser.parse(Lexer.lex(FileIO.readFile(file.getAbsolutePath(), fileName, line), path, 1,
                new ArrayList<>(), 0)));
    }


}
