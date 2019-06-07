package ast.declarations;

import ast.Block;
import ast.expressions.OperationNode;
import ast.statements.ReturnStatementNode;
import org.apache.commons.lang3.NotImplementedException;
import sml.data.tuple.Tuple;

public class LambdaDeclarationNode extends FunctionDeclarationNode {
    private static int lambdasCounter = -1;
    private int lambdaNumber;

    public LambdaDeclarationNode(String[] parameters, OperationNode expression, String fileName, int line) {
        super(parameters, parameters.length-1);
        lambdaNumber = ++lambdasCounter;
        body = new Block(null);
        expression.setParent(body);
        body.addChild(new ReturnStatementNode(expression,fileName,line));
        setFileName(fileName);
        setLine(line);
    }

    @Override
    public String toString() {
        return "Lambda#"+lambdaNumber;
    }

    @Override
    public Object call(Tuple arguments, String callFileName, int callLine) {
        setArguments(arguments,callFileName,callLine);
        var result = body.run();
        if (result instanceof OperationNode)
            return ((OperationNode) result).run();
        throw new NotImplementedException("Return statement somehow wasn't implemented in lambda.");
    }

    @Override
    public FunctionDeclarationNode copy() {
        throw new NotImplementedException("There isn't situation in which lambda should be copied.");
    }
}
