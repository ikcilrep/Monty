func length iterable;
	var counter = 0;
	for _ in iterable;
		counter += 1;
	end;
	return counter;
end;