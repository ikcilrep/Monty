struct Iterable;
	iterable;
	begin = 0;

	func init iterable;
		This.iterable = iterable;
	end;
	
	func get index;
		 counter = 0;
		for x in This;
			if counter == index;
				return x;
			end;
			counter += 1;
		end;
		logError("Iterable doesn't have " +  index + " element");
	end;

	func toList;
		list = [Nothing] * length This;
		i = 0;
		for x in This;
			list.set(i, x);
			i += 1;
		end;
		return list;
	end;
	
	func tail;
		 result = Iterable iterable;
		result.begin = begin + 1;
		return result;
	end;
	
	struct Iterator;
		iterator = iterable.Iterator();
		
		func init;
			i = 0;
			while i != begin & iterator.hasNext();
				iterator.next();
				i += 1;
			end;
		end;
		
		func next;
			return iterator.next();
		end;
		
		func hasNext;
			return iterator.hasNext();
		end;
	end;
end;