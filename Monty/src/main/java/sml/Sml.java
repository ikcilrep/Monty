package sml;

import java.util.HashMap;

import monty.Library;

public class Sml extends Library {

	public Sml(String name) {
		super(name);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setLibrary() {
		var children = getSublibraries();
		children.put("casts", new HashMap<>());
		children.put("math", new HashMap<>());
		children.put("io", new HashMap<>());
		children.put("system", new HashMap<>());
		children.put("data", new HashMap<>());

		var casts = (HashMap<String, Object>) children.get("casts");

		casts.put("toBoolean", new sml.casts.ToBoolean());
		casts.put("toFloat", new sml.casts.ToFloat());
		casts.put("toInt", new sml.casts.ToInt());
		casts.put("toString", new sml.casts.ToString());
		casts.put("toChar", new sml.casts.ToChar());
		casts.put("ord", new sml.casts.Ord());

		var data = (HashMap<String, Object>) children.get("data");
		data.put("array", new HashMap<>());
		data.put("checking", new HashMap<>());
		data.put("string", new HashMap<>());

		var array = (HashMap<String, Object>) data.get("array");
		array.put("arrayOf", new sml.data.array.ArrayOf());
		array.put("extendArray", new sml.data.array.ExtendArray());
		array.put("getFromArray", new sml.data.array.GetFromArray());
		array.put("setInArray", new sml.data.array.SetInArray());

		array.put("lengthOfArray", new sml.data.array.LengthOfArray());
		array.put("subArray", new sml.data.array.Subarray());
		array.put("isInArray", new sml.data.array.isInArray());
		array.put("replaceAllInArray", new sml.data.array.ReplaceAllInArray());
		array.put("replaceFirstInArray", new sml.data.array.ReplaceFirstInArray());
		array.put("replaceLastInArray", new sml.data.array.ReplaceLastInArray());

		var checking = (HashMap<String, Object>) data.get("checking");
		checking.put("isInt", new sml.data.checking.IsInt());
		checking.put("isFloat", new sml.data.checking.IsFloat());
		checking.put("isString", new sml.data.checking.IsString());
		checking.put("isArray", new sml.data.checking.IsArray());

		var string = (HashMap<String, Object>) data.get("string");
		string.put("charAt", new sml.data.string.CharAt());
		string.put("endsWith", new sml.data.string.EndsWith());
		string.put("equalsIgnoreCase", new sml.data.string.EqualsIgnoreCase());
		string.put("lengthOfString", new sml.data.string.LengthOfString());
		string.put("replaceAllInString", new sml.data.string.ReplaceAllInString());
		string.put("startsWith", new sml.data.string.StartsWith());
		string.put("substring", new sml.data.string.Substring());
		string.put("toLowerCase", new sml.data.string.ToLowerCase());
		string.put("toUpperCase", new sml.data.string.ToUpperCase());

		var io = (HashMap<String, Object>) children.get("io");
		io.put("input", new sml.io.Input());

		var math = (HashMap<String, Object>) children.get("math");
		math.put("absFloat", new sml.math.AbsFloat());
		math.put("absInt", new sml.math.AbsInt());
		math.put("PI", new sml.math.PI());
		math.put("powerFloat", new sml.math.PowerFloat());
		math.put("powerInt", new sml.math.PowerInt());
		math.put("sqrtFloat", new sml.math.SqrtFloat());
		math.put("sqrtInt", new sml.math.SqrtInt());

		var system = (HashMap<String, Object>) children.get("system");
		system.put("argv", new sml.system.Argv());

		system.put("exit", new sml.system.Exit());
	}

}
