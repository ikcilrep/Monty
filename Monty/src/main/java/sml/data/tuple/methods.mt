func $eq this other;
    if !(other instanceof Tuple) | other.length() != this.length(); => false;
    i = -1;
    for x in this;
        if x != other.get(i += 1); => false;
    end;
    => true;

func $neq this other; => !(this == other);

func $r_eq other this; => this == other;

func $r_neq other this; => this != other;


struct Iterator;
    counter = 0;
    func hasNext; => counter < length();

    func next;
        result = get counter;
        counter += 1;
        => result;
end;