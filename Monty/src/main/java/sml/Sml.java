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

import java.util.ArrayList;
import java.util.HashMap;

import ast.expressions.OperationNode;
import monty.FileIO;
import monty.Library;
import sml.casts.Ord;
import sml.casts.ToBoolean;
import sml.casts.ToChar;
import sml.casts.ToInt;
import sml.casts.ToFloat;
import sml.casts.ToString;
import sml.language.Run;
import sml.math.E;
import sml.math.Pi;
import sml.system.Argv;
import sml.threading.Sleep;
import sml.time.Millis;
import sml.time.Seconds;

public final class Sml extends Library {
	public static final ArrayList<OperationNode> emptyArgumentList = new ArrayList<>();
	public static String[] paths;
	public static String[] code;
	public static short numberOfFiles;
	static {
		String[] paths_ = { "iterations/range/range.mt", "functional/iterable/iterable.mt",
				"functional/iterable/iterableFunctions.mt" };
		numberOfFiles = (short) paths_.length;
		paths = new String[numberOfFiles];
		code = new String[numberOfFiles];
		paths = paths_;
		int i = 0;
		for (var path : paths)
			code[i++] = FileIO.readFile(Sml.class.getResourceAsStream(path), path);

	}

	public Sml() {
		super("sml");
	}

	@Override
	public void setLibrary() {
		var sml = getSublibraries();
		var casts = new HashMap<String, Object>();
		var math = new HashMap<String, Object>();
		var system = new HashMap<String, Object>();
		var data = new HashMap<String, Object>();
		var threading = new HashMap<String, Object>();
		var time = new HashMap<String, Object>();
		var string = new HashMap<String, Object>();
		var files = new HashMap<String, Object>();
		var language = new HashMap<String, Object>();

		sml.put("casts", casts);
		sml.put("math", math);
		sml.put("system", system);
		sml.put("data", data);
		sml.put("threading", threading);
		sml.put("time", time);
		sml.put("math", math);
		sml.put("files", files);
		sml.put("language", language);

		casts.put("toBoolean", new ToBoolean());
		casts.put("toFloat", new ToFloat());
		casts.put("toInt", new ToInt());
		casts.put("toString", new ToString());
		casts.put("toChar", new ToChar());
		casts.put("ord", new Ord());

		data.put("string", string);

		system.put("Argv", new Argv());

		threading.put("sleep", new Sleep());

		time.put("seconds", new Seconds());
		time.put("millis", new Millis());

		math.put("Pi", new Pi());
		math.put("E", new E());

		language.put("run", new Run());
	}

}
