struct Range;
    var min;
    var max;
    var step;

    func init min max step;
        This.min = min;
        This.max = max;
        This.step = step; 
    end;

    struct Iterator;
        var counter = 0;
        func hasNext;
            counter += step;
            return counter < max;
        end;

        func next;
            return counter;
        end;
    end;
end;