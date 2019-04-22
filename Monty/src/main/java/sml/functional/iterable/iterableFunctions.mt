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

func foldl function, start, iterable;
	var iterator = iterable.Iterator();
	var result;
	if iterator.hasNext();
		result = function.call(start,iterator.next());
	end;
	while iterator.hasNext();
		result = function.call(result,iterator.next());
	end;
	return result;
end;

func foldr function, start, iterable;
	var iterator = iterable.Iterator();
	var result;
	if iterator.hasNext();
		result = iterator.next();
	end;
	while iterator.hasNext();
		result = function.call(iterator.next(), result);
	end;
	return function.call(result, start);
end;