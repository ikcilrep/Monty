fn filter function iterable;
	list = [];
	for x in iterable;
		if function x; => list.add x;
	end;
	=> Iterable list;

fn map function iterable;
	list = [Nothing] * length iterable;
	i = 0;
	for x in iterable;
		list.set(i, function x);
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
	for _ in iterable; => false;
	=> true;

fn foldl function start iterable;
	iterator = iterable.Iterator();
	function recFoldl start;
	    if iterator.hasNext(); => recFoldl(function(start,iterator.next()));
	    => start;
	=> recFoldl(start);

fn foldr function start iterable;
	iterator = iterable.Iterator();
	function recFoldr start;
	    if iterator.hasNext(); => function(iterator.next(), recFoldr(start));
	    => start;
	=> recFoldr(start);