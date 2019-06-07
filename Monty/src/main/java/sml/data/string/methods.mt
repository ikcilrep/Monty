struct Iterator;
    counter = 0;
    func hasNext; => counter < length();

    func next;
        result = charAt counter;
        counter += 1;
        => result;
end;


func $neq this other; => !(this == other);

func $r_eq other this; => this == other;

func $r_neq other this; => this != other;

func $r_mul times this; => this * times;

func $r_a_mul times this; => this *= times;
