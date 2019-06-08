fn filter function iterable;
	list = [];
	for x in iterable;
		doIf(list.add, x,function x);
	end;
	=> list;

fn map function iterable;
	list = [Nothing] * length iterable;
	i = 0;
	for x in iterable;
		list.set(i, function x);
		i += 1;
	end;
	=> list;


fn length iterable;
	counter = 0;
	for _ in iterable;
		counter += 1;
	end;
	=> counter;

fn isEmpty iterable;
	for _ in iterable; => false;
	=> true;

fn foldl function start iterable;
	iterator = iterable.Iterator();
	fn recFoldl start;
	    if iterator.hasNext(); => recFoldl(function(start,iterator.next()));
	    => start;
	=> recFoldl(start);

fn foldr function start iterable;
	iterator = iterable.Iterator();
	fn recFoldr start;
	    if iterator.hasNext(); => function(iterator.next(), recFoldr(start));
	    => start;
	=> recFoldr(start);

fn doIf function argument condition;
    if condition; => function argument;
end;