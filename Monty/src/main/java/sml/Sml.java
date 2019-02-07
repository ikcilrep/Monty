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

import java.util.HashMap;

import monty.Library;
import sml.casts.Ord;
import sml.casts.ToBoolean;
import sml.casts.ToChar;
import sml.casts.ToFloat;
import sml.casts.ToInt;
import sml.casts.ToString;
import sml.data.Get;
import sml.data.Length;
import sml.data.array.NewArray;
import sml.data.checking.IsArray;
import sml.data.checking.IsBoolean;
import sml.data.checking.IsFloat;
import sml.data.checking.IsInt;
import sml.data.checking.IsList;
import sml.data.checking.IsObject;
import sml.data.checking.IsStack;
import sml.data.checking.IsString;
import sml.data.list.NewList;
import sml.data.stack.NewStack;
import sml.data.string.CharAt;
import sml.data.string.EndsWith;
import sml.data.string.EqualsIgnoreCase;
import sml.data.string.Replace;
import sml.data.string.ReplaceFirst;
import sml.data.string.StartsWith;
import sml.data.string.Strlen;
import sml.data.string.Substring;
import sml.data.string.LowerCase;
import sml.data.string.UpperCase;
import sml.errors.LogError;
import sml.io.Input;
import sml.io.Print;
import sml.io.Println;
import sml.math.Abs;
import sml.math.Exp;
import sml.math.Factorial;
import sml.math.Ln;
import sml.math.Log;
import sml.math.Scale;
import sml.math.Max;
import sml.math.Min;
import sml.math.Pow;
import sml.math.Root;
import sml.math.Round;
import sml.system.Argv;
import sml.system.Exit;
import sml.threading.Sleep;
import sml.time.UnixTime;
import sml.time.UnixTimeMillis;

public final class Sml extends Library {

	public Sml() {
		super("sml");
	}

	@Override
	public void setLibrary() {
		var sml = getSublibraries();
		var casts = new HashMap<String, Object>();
		var math = new HashMap<String, Object>();
		var io = new HashMap<String, Object>();
		var system = new HashMap<String, Object>();
		var data = new HashMap<String, Object>();
		var threading = new HashMap<String, Object>();
		var time = new HashMap<String, Object>();
		var checking = new HashMap<String, Object>();
		var string = new HashMap<String, Object>();
		var errors = new HashMap<String, Object>();
		
		sml.put("casts", casts);
		sml.put("math", math);
		sml.put("io", io);
		sml.put("system", system);
		sml.put("data", data);
		sml.put("threading", threading);
		sml.put("time", time);
		sml.put("math", math);
		sml.put("errors", errors);

		casts.put("toBoolean", new ToBoolean());
		casts.put("toFloat", new ToFloat());
		casts.put("toInt", new ToInt());
		casts.put("toString", new ToString());
		casts.put("toChar", new ToChar());
		casts.put("ord", new Ord());

		data.put("Array", new NewArray());
		data.put("Stack", new NewStack());
		data.put("List", new NewList());
		data.put("checking", checking);
		data.put("string", string);
		data.put("length", new Length());
		data.put("get", new Get());
		
		checking.put("isInt", new IsInt());
		checking.put("isFloat", new IsFloat());
		checking.put("isBoolean", new IsBoolean());
		checking.put("isString", new IsString());
		checking.put("isArray", new IsArray());
		checking.put("isList", new IsList());
		checking.put("isStack", new IsStack());
		checking.put("isObject", new IsObject());

		
		string.put("charAt", new CharAt());
		string.put("endsWith", new EndsWith());
		string.put("equalsIgnoreCase", new EqualsIgnoreCase());
		string.put("strlen", new Strlen());
		string.put("replace", new Replace());
		string.put("replaceFirst", new ReplaceFirst());
		string.put("startsWith", new StartsWith());
		string.put("substring", new Substring());
		string.put("lowerCase", new LowerCase());
		string.put("upperCase", new UpperCase());

		io.put("input", new Input());
		io.put("print", new Print());
		io.put("println", new Println());

		system.put("argv", new Argv());
		system.put("exit", new Exit());

		threading.put("sleep", new Sleep());

		time.put("unixTime", new UnixTime());
		time.put("unixTimeMillis", new UnixTimeMillis());

		math.put("pow", new Pow());
		math.put("root", new Root());
		math.put("min", new Min());
		math.put("max", new Max());
		math.put("round", new Round());
		math.put("factorial", new Factorial());
		math.put("scale", new Scale());
		math.put("exp", new Exp());
		math.put("ln", new Ln());
		math.put("log", new Log());
		math.put("abs", new Abs());

		
		errors.put("logError", new LogError());
	}

}
