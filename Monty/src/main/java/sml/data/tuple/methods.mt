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

func $neq this other;
    return !(this == other);
end;

func $r_eq other this;
    return this == other;
end;

func $r_neq other this;
    return this != other;
end;


struct Iterator;
    counter = 0;
    func hasNext;
        return counter < length();
    end;

    func next;
         result = get counter;
        counter += 1;
        return result;
    end;
end;