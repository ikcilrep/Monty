func $eq this other;
    println this, other;
    if !(other instanceof List) | other.length() != this.length();
        return false;
    end;
    var i = 0;
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

func $r_mul other this;
    return this * other;
end;

func $r_a_mul other this;
    return this *= other;
end;

func replace toBeReplaced replacement;
    var i = 0;
    for x in This;
        if toBeReplaced == x;
           set i, replacement;
        end;
        i += 1;
    end;
    return This;
end;

func find value;
    var i = 0;
    for x in This;
        if value == x;
            return i;
        end;
        i += 1;
    end;
    return -1;
end;

func remove value;
    pop find value;
    return This;
end;

func count value;
    var counter = 0;
    for x in This;
        if value == x;
            counter += 1;
        end;
    end;
    return counter;
end;

func sublist first last;
    var result = [Nothing] * (last - first + 1);
    var i = 0;
    while first <= last;
        result.set i, (get(first));
        first += 1;
        i += 1;
    end;
    return result;
end;

struct Iterator;
    var counter = 0;
    func hasNext;
        return counter < length();
    end;

    func next;
        var result = get counter;
        counter += 1;
        return result;
    end;
end;