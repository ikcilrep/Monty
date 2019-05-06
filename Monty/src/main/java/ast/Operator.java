package ast;

import java.util.Set;

public enum Operator {
    ADDITION,
    SUBTRACTION,
    MULTIPLICATION,
    POWER,
    DIVISION,
    MODULO,
    SHIFT_LEFT,
    SHIFT_RIGHT,
    XOR,
    AND,
    OR,
    EQUALS,
    GREATER_THAN,
    LESS_THAN,
    LESS_EQUALS,
    GREATER_EQUALS,
    NOT_EQUALS,
    NEGATION,
    ASSIGNMENT,
    ASSIGNMENT_ADDITION,
    ASSIGNMENT_SUBTRACTION,
    ASSIGNMENT_POWER,
    ASSIGNMENT_MULTIPLICATION,
    ASSIGNMENT_DIVISION,
    ASSIGNMENT_MODULO,
    ASSIGNMENT_SHIFT_LEFT,
    ASSIGNMENT_SHIFT_RIGHT,
    ASSIGNMENT_XOR,
    ASSIGNMENT_AND,
    ASSIGNMENT_OR,
    COMMA,
    DOT,
    JUST;
    private final static Set<Operator> assignmentOperators = Set.of(ASSIGNMENT,
            ASSIGNMENT_ADDITION,
            ASSIGNMENT_SUBTRACTION,
            ASSIGNMENT_POWER,
            ASSIGNMENT_MULTIPLICATION,
            ASSIGNMENT_DIVISION,
            ASSIGNMENT_MODULO,
            ASSIGNMENT_SHIFT_LEFT,
            ASSIGNMENT_SHIFT_RIGHT,
            ASSIGNMENT_XOR,
            ASSIGNMENT_AND,
            ASSIGNMENT_OR);
    public boolean isAssignment() {
        return assignmentOperators.contains(this);
    }
    public static Operator toOperator(String operator) {
        switch (operator) {
            case "+":
                return ADDITION;
            case "-":
                return SUBTRACTION;
            case "*":
                return MULTIPLICATION;
            case "**":
                return POWER;
            case "/":
                return DIVISION;
            case "%":
                return MODULO;
            case "<<":
                return SHIFT_LEFT;
            case ">>":
                return SHIFT_RIGHT;
                case "^":
                return XOR;
            case "&":
                return AND;
            case "|":
                return OR;
            case "==":
                return EQUALS;
            case ">":
                return GREATER_THAN;
            case "<":
                return LESS_THAN;
            case "<=":
                return LESS_EQUALS;
            case ">=":
                return GREATER_EQUALS;
            case "!=":
                return NOT_EQUALS;
            case "!":
                return NEGATION;
            case "=":
                return ASSIGNMENT;
            case "+=":
                return ASSIGNMENT_ADDITION;
            case "-=":
                return ASSIGNMENT_SUBTRACTION;
            case "*=":
                return ASSIGNMENT_MULTIPLICATION;
            case "**=":
                return ASSIGNMENT_POWER;
            case "/=":
                return ASSIGNMENT_DIVISION;
            case "%=":
                return ASSIGNMENT_MODULO;
            case "<<=":
                return ASSIGNMENT_SHIFT_LEFT;
            case ">>=":
                return ASSIGNMENT_SHIFT_RIGHT;
            case "^=":
                return ASSIGNMENT_XOR;
            case "&=":
                return ASSIGNMENT_AND;
            case "|=":
                return ASSIGNMENT_OR;
            case ",":
                return COMMA;
            case ".":
                return DOT;
            case "":
                return JUST;
        }
        return null;
    }

}
