/*
Copyright 2018 Szymon Perlicki

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
import sml.casts.*;
import sml.data.array.*;
import sml.data.list.*;
import sml.data.stack.*;
import sml.data.string.*;
import sml.data.checking.*;
import sml.io.*;
import sml.system.*;
import sml.threading.*;
import sml.math.*;
import sml.time.*;
import monty.Library;

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
