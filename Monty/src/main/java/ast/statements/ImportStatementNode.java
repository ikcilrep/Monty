package ast.statements;

import ast.Block;
import ast.RunnableNode;
import monty.Importing;

public class ImportStatementNode extends RunnableNode {
    private String path;
    private String name;
    private Block block;
    public ImportStatementNode(String path,String name,Block block,String fileName, int line) {
        this.path = path;
        this.name = name;
        this.block = block;
        setFileName(fileName);
        setLine(line);
    }

    @Override
    protected Object run() {
        Importing.importFile(block,path,name,getFileName(),getLine());
        return null;
    }
}
