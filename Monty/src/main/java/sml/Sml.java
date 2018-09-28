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
import java.util.HashMap;

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

		casts.put("toBoolean", new sml.casts.ToBoolean());
		casts.put("toFloat", new sml.casts.ToFloat());
		casts.put("toInt", new sml.casts.ToInt());
		casts.put("toString", new sml.casts.ToString());
		casts.put("toChar", new sml.casts.ToChar());
		casts.put("ord", new sml.casts.Ord());

		var data = (HashMap<String, Object>) children.get("data");
		data.put("array", new HashMap<>());
		data.put("list", new HashMap<>());
		data.put("checking", new HashMap<>());
		data.put("string", new HashMap<>());

		var array = (HashMap<String, Object>) data.get("array");
		array.put("arrayOf", new sml.data.array.ArrayOf());
		array.put("extendArray", new sml.data.array.ExtendArray());
		array.put("getFromArray", new sml.data.array.GetFromArray());
		array.put("setInArray", new sml.data.array.SetInArray());

		array.put("lengthOfArray", new sml.data.array.LengthOfArray());
		array.put("subarray", new sml.data.array.Subarray());
		array.put("isInArray", new sml.data.array.IsInArray());
		array.put("replaceAllInArray", new sml.data.array.ReplaceAllInArray());
		array.put("replaceFirstInArray", new sml.data.array.ReplaceFirstInArray());
		array.put("replaceLastInArray", new sml.data.array.ReplaceLastInArray());

		var list = (HashMap<String, Object>) data.get("list");
		list.put("listOf", new sml.data.list.ListOf());
		list.put("extendList", new sml.data.list.ExtendList());
		list.put("getFromList", new sml.data.list.GetFromList());
		list.put("setInList", new sml.data.list.SetInList());
		
		list.put("lengthOfList", new sml.data.list.LengthOfList());
		list.put("sublist", new sml.data.list.Sublist());
		list.put("isInList", new sml.data.list.IsInList());
		list.put("replaceAllInList", new sml.data.list.ReplaceAllInList());
		list.put("replaceFirstInList", new sml.data.list.ReplaceAllInList());
		list.put("replaceLastInList", new sml.data.list.ReplaceAllInList());

		
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
		io.put("print", new sml.io.Print());
		io.put("println", new sml.io.Println());
		io.put("readFile", new sml.io.ReadFile());
		io.put("writeFile", new sml.io.WriteFile());

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

		var threading = (HashMap<String, Object>) children.get("threading");
		threading.put("sleep", new sml.threading.Sleep());
		threading.put("join", new sml.threading.Join());
		
		var time = (HashMap<String, Object>) children.get("time");
		time.put("unixTime", new sml.time.UnixTime());
		time.put("unixTimeMillis", new sml.time.UnixTimeMillis());


	}

}
