package parser.parsing;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ast.Block;
import ast.declarations.FunctionDeclarationNode;
import lexer.LexerConfig;
import lexer.MontyToken;
import lexer.TokenTypes;
import monty.FileIO;
import monty.Main;
import parser.Identificator;
import parser.MontyException;
import parser.Tokens;

public class Parser {
	public static HashMap<String, Object> libraries;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void setStdlib() {
		libraries = new HashMap<>();
		libraries.put("stdlib", new HashMap<>());

		var stdlib = (HashMap) libraries.get("stdlib");
		stdlib.put("casts", new HashMap<>());
		stdlib.put("math", new HashMap<>());
		stdlib.put("io", new HashMap<>());
		stdlib.put("system", new HashMap<>());
		stdlib.put("data", new HashMap<>());

		var casts = (HashMap) stdlib.get("casts");

		casts.put("toBoolean", new stdlib.casts.ToBoolean());
		casts.put("toFloat", new stdlib.casts.ToFloat());
		casts.put("toInt", new stdlib.casts.ToInt());
		casts.put("toString", new stdlib.casts.ToString());

		var data = (HashMap) stdlib.get("data");
		data.put("array", new HashMap<>());
		data.put("checking", new HashMap<>());
		data.put("string", new HashMap<>());

		var array = (HashMap) data.get("array");
		array.put("arrayOf", new stdlib.data.array.ArrayOf());
		array.put("extendArray", new stdlib.data.array.ExtendArray());
		array.put("getFromArray", new stdlib.data.array.GetFromArray());
		array.put("setInArray", new stdlib.data.array.SetInArray());

		array.put("lengthOfArray", new stdlib.data.array.LengthOfArray());
		array.put("subArray", new stdlib.data.array.Subarray());
		array.put("isInArray", new stdlib.data.array.isInArray());
		array.put("replaceAllInArray", new stdlib.data.array.ReplaceAllInArray());
		array.put("replaceFirstInArray", new stdlib.data.array.ReplaceFirstInArray());
		array.put("replaceLastInArray", new stdlib.data.array.ReplaceLastInArray());

		var checking = (HashMap) data.get("checking");
		checking.put("isInt", new stdlib.data.checking.IsInt());
		checking.put("isFloat", new stdlib.data.checking.IsFloat());
		checking.put("isString", new stdlib.data.checking.IsString());
		checking.put("isArray", new stdlib.data.checking.IsArray());

		var string = (HashMap) data.get("string");
		string.put("charAt", new stdlib.data.string.CharAt());
		string.put("endsWith", new stdlib.data.string.EndsWith());
		string.put("equalsIgnoreCase", new stdlib.data.string.EqualsIgnoreCase());
		string.put("replaceAllInString", new stdlib.data.string.ReplaceAllInString());
		string.put("startsWith", new stdlib.data.string.StartsWith());
		string.put("substring", new stdlib.data.string.Substring());
		string.put("toLowerCase", new stdlib.data.string.ToLowerCase());
		string.put("toUpperCase", new stdlib.data.string.ToUpperCase());

		var io = (HashMap) stdlib.get("io");
		io.put("input", new stdlib.io.Input());

		var math = (HashMap) stdlib.get("math");
		math.put("powerFloat", new stdlib.math.PowerFloat());
		math.put("powerInt", new stdlib.math.PowerInt());

		math.put("sqrtFloat", new stdlib.math.SqrtFloat());
		math.put("sqrtInt", new stdlib.math.SqrtInt());

		var system = (HashMap) stdlib.get("system");
		system.put("argv", new stdlib.system.Argv());

		system.put("exit", new stdlib.system.Exit());
	}

	@SuppressWarnings("unchecked")
	public static void importFile(Block block, List<MontyToken> tokens) {
		var partOfPath = Tokens.getText(tokens.subList(1, tokens.size()));
		var path = new File(Main.path).getParent() + File.separatorChar + partOfPath.replace('.', File.separatorChar)
				+ ".mt";
		if (new File(path).exists()) {
			var text = FileIO.readFile(new File(path).getAbsolutePath());
			var importedTokens = LexerConfig.getLexer(text).getAllTokens();
			var parsed = Parser.parse(importedTokens);
			block.concat(parsed);
		} else {
			var splited = partOfPath.split("\\.");
			var toSearch = libraries;
			Object function = null;
			for (String toImport : splited) {
				if (!toSearch.containsKey(toImport)) {
					new MontyException("There isn't file to import:\t" + partOfPath);
				} else if ((function = toSearch.get(toImport)) instanceof FunctionDeclarationNode) {
					block.addFunction((FunctionDeclarationNode) function);
					break;
				} else {
					toSearch = (HashMap<String, Object>) function;
				}

			}
		}
	}

	public static Block parse(List<MontyToken> tokens) {
		var tokensBeforeSemicolon = new ArrayList<MontyToken>();
		var block = new Block(null);
		for (MontyToken token : tokens) {
			if (token.getType().equals(TokenTypes.SEMICOLON)) {
				if (tokensBeforeSemicolon.size() == 0)
					continue;
				if (Identificator.isExpression(tokensBeforeSemicolon)) {
					AdderToBlock.addExpression(block, tokensBeforeSemicolon);
				} else if (Identificator.isPrintStatement(tokensBeforeSemicolon)) {
					AdderToBlock.addPrintStatement(block, tokensBeforeSemicolon);
				} else if (Identificator.isVariableDeclaration(tokensBeforeSemicolon)) {
					AdderToBlock.addVariableDeclaration(block, tokensBeforeSemicolon);
				} else if (Identificator.isReturnStatement(tokensBeforeSemicolon)) {
					AdderToBlock.addReturnStatement(block, tokensBeforeSemicolon);
				} else if (Identificator.isFunctionDeclaration(tokensBeforeSemicolon)) {
					block = AdderToBlock.addFunctionDeclaration(block, tokensBeforeSemicolon);
				} else if (Identificator.isIfStatement(tokensBeforeSemicolon)) {
					block = AdderToBlock.addIfStatement(block, tokensBeforeSemicolon);
				} else if (Identificator.isElseStatement(tokensBeforeSemicolon)) {
					block = AdderToBlock.addElseStatement(block, tokensBeforeSemicolon);
					if (tokensBeforeSemicolon.size() > 1)
						block = AdderToBlock.addIfStatement(block,
								tokensBeforeSemicolon.subList(1, tokensBeforeSemicolon.size()));
				} else if (Identificator.isWhileStatement(tokensBeforeSemicolon)) {
					block = AdderToBlock.addWhileStatement(block, tokensBeforeSemicolon);
				} else if (Identificator.isImport(tokensBeforeSemicolon)) {
					importFile(block, tokensBeforeSemicolon);
				} else if (Identificator.isChangeToStatement(tokensBeforeSemicolon)) {
					AdderToBlock.addChangeToStatement(block, tokensBeforeSemicolon);
				} else if (Identificator.isEndKeyword(tokensBeforeSemicolon)) {
					var parent = block.getParent();
					if (parent == null)
						new MontyException("Nothing to end!");
					block = block.getParent();
				}
				tokensBeforeSemicolon.clear();
			} else
				tokensBeforeSemicolon.add(token);
		}
		while (true) {
			var parent = block.getParent();
			if (parent == null)
				break;
			block = parent;
		}
		return block;

	}
}
