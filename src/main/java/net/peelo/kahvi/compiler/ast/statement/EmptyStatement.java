package net.peelo.kahvi.compiler.ast.statement;

/**
 * Representation of empty statement, i.e. semicolon.
 */
public final class EmptyStatement extends Statement
{
    public EmptyStatement(int lineNumber, int columnNumber)
    {
        super(lineNumber, columnNumber);
    }

    @Override
    public <R, P> R accept(StatementVisitor<R, P> visitor, P p)
    {
        return visitor.visitEmptyStatement(this, p);
    }

    @Override
    public String toString()
    {
        return ";";
    }
}
