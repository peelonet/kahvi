package net.peelo.kahvi.compiler.ast;

public abstract class Node implements SourcePosition
{
    private final int lineNumber;
    private final int columnNumber;

    public Node(int lineNumber, int columnNumber)
    {
        this.lineNumber = lineNumber;
        this.columnNumber = columnNumber;
    }

    @Override
    public final int getLineNumber()
    {
        return this.lineNumber;
    }

    @Override
    public final int getColumnNumber()
    {
        return this.columnNumber;
    }
}
