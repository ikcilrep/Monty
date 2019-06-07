func $eq this other;
    if !(other instanceof Tuple) | other.length() != this.length();
        return false;
    end;
     i = 0;
    for x in this;
        if x != other.get i;
            return false;
        end;
        i += 1;
    end;
    return true;
end;

func $neq this other -> !(this == other);

func $r_eq other this -> this == other;

func $r_neq other this -> this != other;


struct Iterator;
    counter = 0;
    func hasNext -> counter < length();

    func next;
        result = get counter;
        counter += 1;
        return result;
    end;
end;