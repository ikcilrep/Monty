type Range;
    min; max; step;

    fn init min max step;
        This.min = min;
        This.max = max;
        This.step = step; 
    end;

    type Iterator;
        counter = 0;
        fn hasNext;
            counter += step;
            => counter < max;

        fn next; => counter;
    end;
end;