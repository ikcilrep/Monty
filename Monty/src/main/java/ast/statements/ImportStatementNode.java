package ast.statements;

import ast.Block;
import ast.NodeWithParent;
import monty.Importing;

public class ImportStatementNode extends NodeWithParent {
    private final String path;
    private final String name;
    private Block parent;
    public ImportStatementNode(String path,String name,Block parent,String fileName, int line) {
        this.path = path;
        this.name = name;
        this.parent = parent;
        setFileName(fileName);
        setLine(line);
    }

    @Override
    protected Object run() {
        Importing.importFile(parent,path,name,getFileName(),getLine());
        return null;
    }

    @Override
    protected NodeWithParent copy() {
        return new ImportStatementNode(path,name,parent,getFileName(),getLine());
    }

    @Override
    protected void setParent(Block parent) {
        this.parent = parent;
    }
}
