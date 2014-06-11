package net.peelo.kahvi.compiler.ast.statement;

import net.peelo.kahvi.compiler.util.SourcePosition;

public abstract class ContinuableStatement extends BreakableStatement
{
    public ContinuableStatement(SourcePosition position)
    {
        super(position);
    }
}
