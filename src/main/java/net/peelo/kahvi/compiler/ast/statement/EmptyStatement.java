package net.peelo.kahvi.compiler.ast.statement;

import net.peelo.kahvi.compiler.util.SourcePosition;

/**
 * Representation of empty statement, i.e. semicolon.
 */
public final class EmptyStatement extends Statement
{
    public EmptyStatement(SourcePosition position)
    {
        super(position);
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
