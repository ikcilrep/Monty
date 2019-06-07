type Iterable;
	iterable;
	begin = 0;

	fn init iterable;
		This.iterable = iterable;
	end;
	
	fn get index;
		counter = 0;
		for x in This;
			if counter == index; => x;
			counter += 1;
		end;
		logError("Iterable doesn't have " +  index + " element");
	end;

	fn toList;
		list = [Nothing] * length This;
		i = 0;
		for x in This;
			list.set(i, x);
			i += 1;
		end;
		return list;
	end;
	
	fn tail;
		result = Iterable iterable;
		result.begin = begin + 1;
		=> result;

	type Iterator;
		iterator = iterable.Iterator();
		
		fn init;
			i = 0;
			while i != begin & iterator.hasNext();
				iterator.next();
				i += 1;
			end;
		end;
		
		fn next; => iterator.next();
		
		fn hasNext; => iterator.hasNext();
	end;
end;