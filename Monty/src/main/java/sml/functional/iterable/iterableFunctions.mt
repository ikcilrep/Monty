func filter function iterable;
	list = [];
	for x in iterable;
		if function x;
			list.add x;
		end;
	end;
	return Iterable list;
end;

func map function iterable;
	list = [Nothing] * length iterable;
	i = 0;
	for x in iterable;
		list.set(i, function x);
		i += 1;
	end;
	=> Iterable list;


func length iterable;
	counter = 0;
	for _ in iterable;
		counter += 1;
	end;
	=> counter;

func isEmpty iterable;
	for _ in iterable;
		=> false;
	=> true;

func foldl function start iterable;
	iterator = iterable.Iterator();
	result;
	if iterator.hasNext();
		result = function(start, iterator.next());
	end;
	while iterator.hasNext();
		result = function(result, iterator.next());
	end;
	=> result;

func foldr function start iterable;
	iterator = iterable.Iterator();
	result;
	if iterator.hasNext();
		result = iterator.next();
	end;
	while iterator.hasNext();
		result = function(iterator.next(), result);
	end;
	=> function(result, start);