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
import sml.casts.*;
import sml.data.Get;
import sml.data.*;
import sml.data.string.*;
import sml.data.checking.*;
import sml.data.array.NewArray;
import sml.data.list.NewList;
import sml.data.stack.NewStack;
import sml.errors.*;
import sml.files.*;
import sml.io.*;
import sml.math.*;
import sml.system.*;
import sml.threading.*;
import sml.time.*;

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
		var files = new HashMap<String, Object>();

		sml.put("casts", casts);
		sml.put("math", math);
		sml.put("io", io);
		sml.put("system", system);
		sml.put("data", data);
		sml.put("threading", threading);
		sml.put("time", time);
		sml.put("math", math);
		sml.put("errors", errors);
		sml.put("files", files);

		casts.put("toBoolean", new ToBoolean());
		casts.put("toReal", new ToReal());
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
		checking.put("isReal", new IsReal());
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
		string.put("split", new Split());

		io.put("input", new Input());
		io.put("print", new Print());
		io.put("println", new Println());

		system.put("argv", new Argv());
		system.put("argc", new Argc());
		system.put("exit", new Exit());

		threading.put("sleep", new Sleep());

		time.put("unixTime", new UnixTime());
		time.put("unixTimeMillis", new UnixTimeMillis());

		math.put("pi", new Pi());
		math.put("e", new E());
		math.put("pow", new Pow());
		math.put("root", new Root());
		math.put("min", new Min());
		math.put("max", new Max());
		math.put("random", new Random());
		math.put("round", new Round());
		math.put("factorial", new Factorial());
		math.put("scale", new Scale());
		math.put("exp", new Exp());
		math.put("log", new Log());
		math.put("abs", new Abs());

		errors.put("logError", new LogError());

		files.put("separator", new Separator());
		files.put("listDir", new ListDir());
		files.put("absPath", new AbsPath());
		files.put("write", new Write());
		files.put("read", new Read());
		files.put("exists", new Exists());
		files.put("isAbsolute", new IsAbsolute());
		files.put("isDir", new IsDir());
		files.put("isFile", new IsFile());

	}

}
