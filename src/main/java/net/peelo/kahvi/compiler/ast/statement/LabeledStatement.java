package net.peelo.kahvi.compiler.ast.statement;

import net.peelo.kahvi.compiler.util.Name;

public final class LabeledStatement extends Statement
{
    private final Name label;
    private final Statement statement;

    public LabeledStatement(Name label,
                            Statement statement,
                            int lineNumber,
                            int columnNumber)
    {
        super(columnNumber, lineNumber);
        this.label = label;
        (this.statement = statement).setEnclosingScope(this);
    }

    public Name getLabel()
    {
        return this.label;
    }

    public Statement getStatement()
    {
        return this.statement;
    }

    @Override
    public <R, P> R accept(StatementVisitor<R, P> visitor, P p)
    {
        return visitor.visitLabeledStatement(this, p);
    }

    @Override
    public String toString()
    {
        return this.label + ": " + this.statement;
    }
}
