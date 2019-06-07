fn $eq this other;
    if !(other instanceof Tuple) | other.length() != this.length(); => false;
    i = -1;
    for x in this;
        if x != other.get(i += 1); => false;
    end;
    => true;

fn $neq this other; => !(this == other);

fn $r_eq other this; => this == other;

fn $r_neq other this; => this != other;


type Iterator;
    counter = 0;
    fn hasNext; => counter < length();

    fn next;
        result = get counter;
        counter += 1;
        => result;
end;