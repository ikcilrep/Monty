func filter iterable, function;
	var list = [];
	for x in iterable;
		if function.call(x);
			list.add(x);
		end;
	end;
	return Iterable(list);
end;

func map iterable, function;
	var list = [Nothing] * length(iterable);
	var i = 0;
	for x in iterable;
		list.set(i, function.call(x));
		i += 1;
	end;
	return Iterable(list);
end;

func length iterable;
	var counter = 0;
	for _ in iterable;
		counter += 1;
	end;
	return counter;
end;

func isEmpty iterable;
	for _ in iterable;
		return false;
	end;
	return true;
end;
