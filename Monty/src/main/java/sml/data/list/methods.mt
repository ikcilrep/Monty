func $eq this other;
    println this, other;
    if !(other instanceof List) | other.length() != this.length();
        return false;
    end;
    i = 0;
    for x in this;
        if x != other.get i;
            return false;
        end;
        i += 1;
    end;
    => true;

func $neq this other; => !(this == other);

func $r_eq other this; => this == other;

func $r_neq other this; => this != other;

func $r_mul other this; => this * other;

func $r_a_mul other this; => this *= other;

func replace toBeReplaced replacement;
    i = 0;
    for x in This;
        if toBeReplaced == x;
           set(i, replacement);
        end;
        i += 1;
    end;
    => This;

func find value;
    i = 0;
    for x in This;
        if value == x; => i;
        i += 1;
    end;
    => -1;

func remove value;
    pop find value;
    => This;

func count value;
    counter = 0;
    for x in This;
        if value == x;
            counter += 1;
        end;
    end;
    => counter;

func sublist first last;
    result = [Nothing] * (last - first + 1);
    i = 0;
    while first <= last;
        result.set(i, get first);
        first += 1;
        i += 1;
    end;
    => result;

struct Iterator;
    counter = 0;
    func hasNext; => counter < length();

    func next;
        result = get counter;
        counter += 1;
        => result;
end;