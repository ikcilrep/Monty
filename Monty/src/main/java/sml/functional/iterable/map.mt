func map iterable, function;
	var list = [Nothing] * length(iterable);
	var i = 0;
	for x in iterable;
		list.set(i, function.call(x));
		i += 1;
	end;
	return Iterable(list);
end;