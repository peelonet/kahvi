package net.peelo.kahvi.compiler.ast.statement;

public abstract class BreakableStatement extends Statement
{
    public BreakableStatement(int lineNumber, int columnNumber)
    {
        super(lineNumber, columnNumber);
    }
}
