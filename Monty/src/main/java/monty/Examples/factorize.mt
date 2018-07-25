import stdlib.math.sqrtInt;
import stdlib.io.input;
import stdlib.casts.toInt;
import stdlib.casts.toFloat;

import stdlib.casts.toString;
import stdlib.math.powerInt;

var static int n toInt(input())  =;

var static int endd sqrtInt(n) =;
var static int start 0 =;

func float setBorders float e;
	if sqrtInt(toInt(e)) 0 == sqrtInt(toInt(e)) sqrtInt(sqrtInt(toInt(e))) - 0 == ||;
		return 0;
	end;
	endd sqrtInt(toInt(e)) -=;
	start endd sqrtInt(endd) - +=;
	println endd start -;
	return setBorders(endd start -);
end;

setBorders(toFloat(endd));
println start;
println endd;

while endd start >=;
	if n start % 0 ==;
		println toString(start) " * " toString(n start /) + +;
	end;
	start 1 +=;
end;

