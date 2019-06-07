package parser;

public class RecognizedFunctionPrototype {
    public boolean isShortFunction() {
        return isShortFunction;
    }


    private boolean isShortFunction;

    public boolean isLongFunction() {
        return isLongFunction;
    }

    public boolean isAnyFunction() {
        return isLongFunction || isShortFunction;
    }
    private boolean isLongFunction;

    public int getEndOfParameters() {
        return endOfParameters;
    }

    private int endOfParameters;
    RecognizedFunctionPrototype(int endOfParameters, boolean isShortFunction) {
        this.endOfParameters = endOfParameters;
        isLongFunction = !isShortFunction;
        this.isShortFunction = isShortFunction;
    }

    RecognizedFunctionPrototype() {
        isLongFunction = false;
        isShortFunction = false;

    }
}
