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

import monty.FileIO;
import sml.casts.*;
import sml.language.Run;
import sml.math.E;
import sml.math.Exp;
import sml.math.Factorial;
import sml.math.Pi;
import sml.system.Argv;
import sml.threading.Sleep;
import sml.time.Millis;
import sml.time.Seconds;

import java.util.HashMap;

public final class Sml {

    public final static String[] CODE;
    public final static short NUMBER_OF_FILES;
    private final static HashMap<String, Object> children;
    public static String[] paths;

    static {
        String[] paths_ = {"iterations/range/range.mt", "functional/iterable/iterable.mt",
                "functional/iterable/iterableFunctions.mt"};
        NUMBER_OF_FILES = (short) paths_.length;
        paths = new String[NUMBER_OF_FILES];
        CODE = new String[NUMBER_OF_FILES];
        paths = paths_;
        int i = 0;
        for (var path : paths)
            CODE[i++] = FileIO.readFile(Sml.class.getResourceAsStream(path), path);
        children = new HashMap<>();
        var casts = new HashMap<String, Object>();
        var math = new HashMap<String, Object>();
        var system = new HashMap<String, Object>();
        var data = new HashMap<String, Object>();
        var threading = new HashMap<String, Object>();
        var time = new HashMap<String, Object>();
        var string = new HashMap<String, Object>();
        var files = new HashMap<String, Object>();
        var language = new HashMap<String, Object>();

        children.put("casts", casts);
        children.put("math", math);
        children.put("system", system);
        children.put("data", data);
        children.put("threading", threading);
        children.put("time", time);
        children.put("files", files);
        children.put("language", language);

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
        math.put("exp", new Exp());
        math.put("factorial", new Factorial());


        language.put("run", new Run());
    }

    public static HashMap<String, Object> getChildren() {
        return children;
    }

}
