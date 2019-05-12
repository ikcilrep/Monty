struct Iterator;
    var counter = 0;
    func hasNext;
        return counter < length();
    end;

    func next;
        var result = charAt counter;
        counter += 1;
        return result;
    end;
end;