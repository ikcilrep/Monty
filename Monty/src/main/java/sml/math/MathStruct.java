package sml.math;

import sml.data.StaticStruct;

import java.util.HashMap;

public final class MathStruct {
    private static StaticStruct struct = new StaticStruct();

    static {
        struct.setFunctions(new HashMap<>());
        new Abs(struct);
        new Exp(struct);
        new Round(struct);
        new Factorial(struct);
    }

    public static StaticStruct getStruct() {
        return struct;
    }

}
