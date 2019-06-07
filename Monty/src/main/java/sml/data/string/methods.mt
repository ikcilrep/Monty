type Iterator;
    counter = 0;
    fn hasNext; => counter < length();

    fn next;
        result = charAt counter;
        counter += 1;
        => result;
end;


fn $neq this other; => !(this == other);

fn $r_eq other this; => this == other;

fn $r_neq other this; => this != other;

fn $r_mul times this; => this * times;

fn $r_a_mul times this; => this *= times;
