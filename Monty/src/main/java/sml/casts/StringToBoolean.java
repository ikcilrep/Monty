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

public class StringToBoolean {

	public static Boolean stringToBoolean(String str) {
		Boolean doesstrEqualsTrue = str.equalsIgnoreCase("true");
		if (doesstrEqualsTrue || str.equalsIgnoreCase("false"))
			return doesstrEqualsTrue;
		else
			new MontyException("Unknown format for boolean type:\t" + str);
		return null;
	}

}
