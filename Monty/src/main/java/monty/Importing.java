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
    private final static Path FILE_ABSOLUTE_PATH = Paths.get(Main.path).getParent();
    private final static String MAIN_PATH = new File(Main.path).isAbsolute() ? "" : Paths.get("").toAbsolutePath().toString();
    private final static String MAIN_FILE_LOCATION = MAIN_PATH
            + (MAIN_PATH.endsWith(File.separator) || MAIN_PATH.isEmpty() ? "" : File.separator)
            + (FILE_ABSOLUTE_PATH == null ? "" : FILE_ABSOLUTE_PATH) + File.separator;


    @SuppressWarnings("unchecked")
    private static void importAllElementsFromSmlChildren(Block block, HashMap<String, Object> addFrom,String name, String fileName, int line) {
        Block sml;
        if (!block.hasNamespace(name))
            block.addNamespace(name, sml = new Block(block), fileName,line);
        else {
            sml = block.getNamespace(name);
        }
        for (Object value : addFrom.values())
            if (value instanceof FunctionDeclarationNode)
                sml.addFunction((FunctionDeclarationNode) value, fileName,line);
            else if (value instanceof VariableDeclarationNode)
                sml.addVariable((VariableDeclarationNode) value, fileName,line);
            else
                importAllElementsFromSmlChildren(sml, (HashMap<String, Object>) value,name, fileName,line);
    }

    @SuppressWarnings("unchecked")
    private static void importElementFromSml(Block block, String[] split, String path, String name,String fileName, int line) {
        var children = Sml.getChildren();
        Object functionVariableOrSubLibrary;

        for (int i = 1; i < split.length; i++) {
            var toImport = split[i];
            if (!children.containsKey(toImport))
                new LogError("There isn't file to import:\t" + path, fileName, line);
            else if ((functionVariableOrSubLibrary = children.get(toImport)) instanceof FunctionDeclarationNode) {
                block.addFunction((FunctionDeclarationNode) functionVariableOrSubLibrary, fileName, line);
                break;
            } else if (functionVariableOrSubLibrary instanceof VariableDeclarationNode) {
                block.addVariable((VariableDeclarationNode) functionVariableOrSubLibrary, fileName,line);
                break;
            } else if (i + 1 >= split.length)
                importAllElementsFromSmlChildren(block, (HashMap<String, Object>) functionVariableOrSubLibrary,name,
                        fileName,line);
            else
                children = (HashMap<String, Object>) functionVariableOrSubLibrary;
        }
    }

    public static String tokensToPath(ArrayList<Token> tokensBeforeSemicolon) {
        var stringBuilder = new StringBuilder();
        for(int i = 1; i < tokensBeforeSemicolon.size()-3;i++) {
            stringBuilder.append(tokensBeforeSemicolon.get(i).getText());
            stringBuilder.append(File.separator);
        }
        stringBuilder.append(tokensBeforeSemicolon.get(tokensBeforeSemicolon.size() -3));
        return stringBuilder.toString();
    }

    public static void importFile(Block block, String partOfPath,String name, String fileName, int line) {
        var path = MAIN_FILE_LOCATION + partOfPath;
        var file = new File(path + ".mt");
        var parent_file = new File(file.getParent() + ".mt");
        var directory = new File(path);

        if (directory.exists() && directory.isDirectory())
            importFilesFromDirectory(block, directory, name, fileName, line);
        else if (file.exists() && file.isFile())
            importWholeFile(block, file.getPath(), name, fileName, line);
        else if (parent_file.exists() && parent_file.isFile()) {
            importSpecifiedElementFromBlock(block,
                    IOBlocks.readBlockFromFile(parent_file.getAbsolutePath()),
                    parent_file.getPath(), file.getName().substring(0, file.getName().length() - 3), fileName, line);
        } else {
            var split = partOfPath.split("\\.");
            if (!split[0].equals("sml"))
                new LogError("There isn't file to import:\t" + path, fileName, line);
            importElementFromSml(block, split, path, name,fileName,line);
        }
    }

    private static void importFilesFromDirectory(Block block, File directory, String name,String fileName, int line) {
        for (File fileEntry : Objects.requireNonNull(directory.listFiles())) {
            if (fileEntry.isDirectory())
                importFilesFromDirectory(block, fileEntry,name, fileName, line);
            else if (fileEntry.getName().endsWith(".mt"))
                importWholeFile(block, fileEntry.getAbsolutePath(),name, fileName, line);
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

    private static void importWholeFile(Block block, String path,String name, String fileName, int line) {
        var importedBlock =IOBlocks.readBlockFromFile(new File(path).getAbsolutePath());
        importedBlock.run();
        block.addNamespace(name,importedBlock,fileName,line);
    }


}
