package net.peelo.kahvi.compiler.ast.statement;

public abstract class ContinuableStatement extends BreakableStatement
{
    public ContinuableStatement(int lineNumber, int columnNumber)
    {
        super(lineNumber, columnNumber);
    }
}
