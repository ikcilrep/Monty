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

package sml.casts;

import parser.MontyException;

public class StringToFloat {

	public static Float stringToFloat(String str) {
		if (str.matches("[+-]?[0-9]+\\.[0-9]+"))
			return Float.parseFloat(str);
		else if (str.matches("[+-]?[0-9]+"))
			return (Float) (float) Integer.parseInt(str);
		else
			new MontyException("Unknown number format for float type:\t" + str);
		return null;
	}

}
