func $eq this other;
    if other.length() != this.length();
        return false;
    end;
    var i = 0;
    for x in this;
        if x != other.get(i);
            return false;
        end;
        i += 1;
    end;
    return true;
end;