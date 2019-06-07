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
            return counter < max;
        end;

        fn next;
            return counter;
        end;
    end;
end;