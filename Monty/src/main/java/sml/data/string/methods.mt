struct Iterator;
    counter = 0;
    func hasNext;
        return counter < length();
    end;

    func next;
         result = charAt counter;
        counter += 1;
        return result;
    end;
end;


func $neq this other;
    return !(this == other);
end;

func $r_eq other this;
    return this == other;
end;

func $r_neq other this;
    return this != other;
end;

func $r_mul times this;
    return this * times;
end;

func $r_a_mul times this;
    return this *= times;
end;