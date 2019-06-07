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
import ast.declarations.VariableDeclarationNode;
import ast.expressions.OperationNode;
import lexer.Lexer;
import parser.parsing.Parser;
import sml.NativeFunctionDeclarationNode;
import sml.Sml;
import sml.data.tuple.ToTuple;
import sml.data.tuple.Tuple;
import sml.io.Input;
import sml.io.Print;
import sml.io.Println;

import java.util.ArrayList;

public class IOBlocks {
    public final static VariableDeclarationNode nothing;
    private final static NativeFunctionDeclarationNode abs;
    private final static NativeFunctionDeclarationNode list;
    private final static NativeFunctionDeclarationNode logError;
    private final static NativeFunctionDeclarationNode print;
    private final static NativeFunctionDeclarationNode println;
    private final static NativeFunctionDeclarationNode round;
    private final static NativeFunctionDeclarationNode tuple;

    private final static NativeFunctionDeclarationNode input;
    private final static Block[] writtenInMonty;

    static {
        abs = new sml.math.Abs();
        list = new sml.data.list.NewList();
        logError = new sml.errors.LogError();
        nothing = new sml.data.returning.Nothing();
        writtenInMonty = new Block[Sml.NUMBER_OF_FILES];
        int i = 0;
        for (var code : Sml.CODE)
            writtenInMonty[i] = Parser.parse(Lexer.lex(code, Sml.paths[i++]));

        print = new Print();
        println = new Println();
        round = new sml.math.Round();
        tuple = new ToTuple();
        input = new Input();
    }

    private static void autoImport(Block block) {
        var fileName = "command line";
        var line = 1;
        block.addVariable(nothing, fileName, line);
        block.addFunction(abs, fileName, line);
        block.addFunction(list, fileName, line);
        block.addFunction(print, fileName, line);
        block.addFunction(println, fileName, line);
        block.addFunction(round, fileName, line);
        block.addFunction(input, fileName, line);
        block.addFunction(logError, fileName, line);
        block.addFunction(tuple, fileName, line);

        for (var parsed : writtenInMonty)
            block.concat(parsed, fileName, line);
    }

    static void readAndRunBlockFromFile(String path) {
        var block = readBlockFromFile(path, "command line", 1);
        OperationNode.emptyTuple = new Tuple();
        block.run();
    }

    static Block readBlockFromFile(String path, String fileName, int line) {
        var block = Parser
                .parse(Lexer.lex(FileIO.readFile(path, fileName, line), path, 1, new ArrayList<>(), 0));
        autoImport(block);
        return block;
    }
}
