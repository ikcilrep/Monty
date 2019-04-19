func filter iterable, function;
	var list = [];
	for x in iterable;
		if function.call(x);
			list.add(x);
		end;
	end;
	return Iterable(list);
end;