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

package sml;

import java.io.Serializable;
import java.util.HashMap;

import monty.Library;
import sml.casts.Ord;
import sml.casts.ToArray;
import sml.casts.ToBoolean;
import sml.casts.ToChar;
import sml.casts.ToFloat;
import sml.casts.ToInt;
import sml.casts.ToList;
import sml.casts.ToStack;
import sml.casts.ToString;
import sml.data.array.ArrayOf;
import sml.data.array.ExtendArray;
import sml.data.array.GetFromArray;
import sml.data.array.IsInArray;
import sml.data.array.LengthOfArray;
import sml.data.array.ReplaceAllInArray;
import sml.data.array.ReplaceFirstInArray;
import sml.data.array.ReplaceLastInArray;
import sml.data.array.SetInArray;
import sml.data.array.Subarray;
import sml.data.checking.IsArray;
import sml.data.checking.IsFloat;
import sml.data.checking.IsInt;
import sml.data.checking.IsList;
import sml.data.checking.IsStack;
import sml.data.checking.IsString;
import sml.data.list.ExtendList;
import sml.data.list.GetFromList;
import sml.data.list.IsInList;
import sml.data.list.LengthOfList;
import sml.data.list.ListOf;
import sml.data.list.ReplaceAllInList;
import sml.data.list.ReplaceFirstInList;
import sml.data.list.ReplaceLastInList;
import sml.data.list.SetInList;
import sml.data.list.Sublist;
import sml.data.stack.NewStack;
import sml.data.stack.Peek;
import sml.data.stack.Pop;
import sml.data.string.CharAt;
import sml.data.string.EndsWith;
import sml.data.string.EqualsIgnoreCase;
import sml.data.string.LengthOfString;
import sml.data.string.ReplaceAllInString;
import sml.data.string.StartsWith;
import sml.data.string.Substring;
import sml.data.string.ToLowerCase;
import sml.data.string.ToUpperCase;
import sml.io.Input;
import sml.io.Print;
import sml.io.Println;
import sml.io.ReadFile;
import sml.io.WriteFile;
import sml.math.AbsInt;
import sml.math.PI;
import sml.math.PowerFloat;
import sml.math.PowerInt;
import sml.math.SqrtFloat;
import sml.math.SqrtInt;
import sml.system.Argv;
import sml.system.Exit;
import sml.threading.Join;
import sml.threading.Sleep;
import sml.time.UnixTime;
import sml.time.UnixTimeMillis;

public class Sml extends Library implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9055473117351035776L;

	public Sml() {
		super("sml");
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
		children.put("threading", new HashMap<>());
		children.put("time", new HashMap<>());
		var casts = (HashMap<String, Object>) children.get("casts");

		casts.put("toBoolean", new ToBoolean());
		casts.put("toArray", new ToArray());
		casts.put("toList", new ToList());
		casts.put("toStack", new ToStack());

		casts.put("toFloat", new ToFloat());
		casts.put("toInt", new ToInt());
		casts.put("toString", new ToString());
		casts.put("toChar", new ToChar());
		casts.put("ord", new Ord());

		var data = (HashMap<String, Object>) children.get("data");
		data.put("array", new HashMap<>());
		data.put("list", new HashMap<>());
		data.put("checking", new HashMap<>());
		data.put("string", new HashMap<>());
		data.put("stack", new HashMap<>());

		var array = (HashMap<String, Object>) data.get("array");
		array.put("arrayOf", new ArrayOf());
		array.put("extendArray", new ExtendArray());
		array.put("getFromArray", new GetFromArray());
		array.put("setInArray", new SetInArray());

		array.put("lengthOfArray", new LengthOfArray());
		array.put("subarray", new Subarray());
		array.put("isInArray", new IsInArray());
		array.put("replaceAllInArray", new ReplaceAllInArray());
		array.put("replaceFirstInArray", new ReplaceFirstInArray());
		array.put("replaceLastInArray", new ReplaceLastInArray());

		var list = (HashMap<String, Object>) data.get("list");
		list.put("listOf", new ListOf());
		list.put("extendList", new ExtendList());
		list.put("getFromList", new GetFromList());
		list.put("setInList", new SetInList());

		list.put("lengthOfList", new LengthOfList());
		list.put("sublist", new Sublist());
		list.put("isInList", new IsInList());
		list.put("replaceAllInList", new ReplaceAllInList());
		list.put("replaceFirstInList", new ReplaceFirstInList());
		list.put("replaceLastInList", new ReplaceLastInList());

		var checking = (HashMap<String, Object>) data.get("checking");
		checking.put("isInt", new IsInt());
		checking.put("isFloat", new IsFloat());
		checking.put("isString", new IsString());
		checking.put("isArray", new IsArray());
		checking.put("isList", new IsList());
		checking.put("isStack", new IsStack());

		var string = (HashMap<String, Object>) data.get("string");
		string.put("charAt", new CharAt());
		string.put("endsWith", new EndsWith());
		string.put("equalsIgnoreCase", new EqualsIgnoreCase());
		string.put("lengthOfString", new LengthOfString());
		string.put("replaceAllInString", new ReplaceAllInString());
		string.put("startsWith", new StartsWith());
		string.put("substring", new Substring());
		string.put("toLowerCase", new ToLowerCase());
		string.put("toUpperCase", new ToUpperCase());

		var stack = (HashMap<String, Object>) data.get("stack");
		stack.put("newStack", new NewStack());
		stack.put("pop", new Pop());
		stack.put("peek", new Peek());

		var io = (HashMap<String, Object>) children.get("io");
		io.put("input", new Input());
		io.put("print", new Print());
		io.put("println", new Println());
		io.put("readFile", new ReadFile());
		io.put("writeFile", new WriteFile());

		var math = (HashMap<String, Object>) children.get("math");
		math.put("absFloat", new sml.math.AbsFloat());
		math.put("absInt", new AbsInt());
		math.put("PI", new PI());
		math.put("powerFloat", new PowerFloat());
		math.put("powerInt", new PowerInt());
		math.put("sqrtFloat", new SqrtFloat());
		math.put("sqrtInt", new SqrtInt());

		var system = (HashMap<String, Object>) children.get("system");
		system.put("argv", new Argv());
		system.put("exit", new Exit());

		var threading = (HashMap<String, Object>) children.get("threading");
		threading.put("sleep", new Sleep());
		threading.put("join", new Join());

		var time = (HashMap<String, Object>) children.get("time");
		time.put("unixTime", new UnixTime());
		time.put("unixTimeMillis", new UnixTimeMillis());

	}

}
