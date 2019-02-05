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

package sml.casts;

import java.math.BigInteger;

import parser.LogError;

public final class StringToInt {

	public static BigInteger stringToInt(String str, String fileName, int line) {
		if (str.matches("[+-]?[0-9]+\\.[0-9]+"))
			return new BigInteger(str.split("\\.")[0]);
		else if (str.matches("[+-]?[0-9]+"))
			return new BigInteger(str);
		else
			new LogError("Unknown number format for integer type:\t" + str, fileName, line);
		return null;
	}

}
