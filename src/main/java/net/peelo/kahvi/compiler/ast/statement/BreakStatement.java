package net.peelo.kahvi.compiler.ast.statement;

import net.peelo.kahvi.compiler.util.Name;

public final class BreakStatement extends Statement
{
    private final Name label;

    public BreakStatement(int lineNumber, int columnNumber)
    {
        this(null, lineNumber, columnNumber);
    }

    public BreakStatement(Name label, int lineNumber, int columnNumber)
    {
        super(lineNumber, columnNumber);
        this.label = label;
    }

    public Name getLabel()
    {
        return this.label;
    }

    @Override
    public <R, P> R accept(StatementVisitor<R, P> visitor, P p)
    {
        return visitor.visitBreakStatement(this, p);
    }

    @Override
    public String toString()
    {
        if (this.label == null)
        {
            return "break;";
        } else {
            return "break " + this.label + ";";
        }
    }
}