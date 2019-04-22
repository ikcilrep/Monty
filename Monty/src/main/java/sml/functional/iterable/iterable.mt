struct Iterable;
	var iterable;
	var begin = 0;

	func init iterable;
		This.iterable = iterable;
	end;
	
	func get index;
		var counter = 0;
		for x in This;
			if counter == index;
				return x;
			end;
			counter += 1;
		end;
		logError("Iterable doesn't have " +  index + " element");
	end;
	
	func toList;
		var list = [Nothing] * length(This);
		var i = 0;
		for x in This;
			list.set(i, x);
			i += 1;
		end;
		return list;
	end;
	
	func tail;
		var result = Iterable(iterable);
		result.begin = begin + 1;
		return result;
	end;
	
	func foldl function, start;
		if isEmpty(This);
			return start;
		end;
		return tail().foldl(function,function.call(start,get(0)));
	end;
	
	struct Iterator;
		var iterator = iterable.Iterator();
		
		func init;
			var i = 0;
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