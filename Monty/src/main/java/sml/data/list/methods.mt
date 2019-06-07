fn $eq this other;
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

fn $neq this other; => !(this == other);

fn $r_eq other this; => this == other;

fn $r_neq other this; => this != other;

fn $r_mul other this; => this * other;

fn $r_a_mul other this; => this *= other;

fn replace toBeReplaced replacement;
    i = 0;
    for x in This;
        if toBeReplaced == x;
           set(i, replacement);
        end;
        i += 1;
    end;
    => This;

fn find value;
    i = 0;
    for x in This;
        if value == x; => i;
        i += 1;
    end;
    => -1;

fn remove value;
    pop find value;
    => This;

fn count value;
    counter = 0;
    for x in This;
        if value == x;
            counter += 1;
        end;
    end;
    => counter;

fn sublist first last;
    result = [Nothing] * (last - first + 1);
    i = 0;
    while first <= last;
        result.set(i, get first);
        first += 1;
        i += 1;
    end;
    => result;

type Iterator;
    counter = 0;
    fn hasNext; => counter < length();

    fn next;
        result = get counter;
        counter += 1;
        => result;
end;