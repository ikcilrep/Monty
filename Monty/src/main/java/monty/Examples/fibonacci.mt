func int fib int n;
	if n 0 ==;
		return 0;
	end;
	if n 1 ==;
		return 1;
	end;
	return fib(n 1-) fib (n 2 -) +;
end;

print "Podaj numer liczby w ci¹gu fibonacciego:\t" ;
var int n stringToInt(input()) =;
println fib(n);