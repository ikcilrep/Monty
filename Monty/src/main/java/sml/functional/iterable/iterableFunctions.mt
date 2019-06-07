fn filter fntion iterable;
	list = [];
	for x in iterable;
		if fntion x;
			list.add x;
		end;
	end;
	return Iterable list;
end;

fn map fntion iterable;
	list = [Nothing] * length iterable;
	i = 0;
	for x in iterable;
		list.set(i, fntion x);
		i += 1;
	end;
	=> Iterable list;


fn length iterable;
	counter = 0;
	for _ in iterable;
		counter += 1;
	end;
	=> counter;

fn isEmpty iterable;
	for _ in iterable;
		=> false;
	=> true;

fn foldl fntion start iterable;
	iterator = iterable.Iterator();
	result;
	if iterator.hasNext();
		result = fntion(start, iterator.next());
	end;
	while iterator.hasNext();
		result = fntion(result, iterator.next());
	end;
	=> result;

fn foldr fntion start iterable;
	iterator = iterable.Iterator();
	result;
	if iterator.hasNext();
		result = iterator.next();
	end;
	while iterator.hasNext();
		result = fntion(iterator.next(), result);
	end;
	=> fntion(result, start);