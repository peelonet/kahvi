package net.peelo.kahvi.compiler.ast.statement;

import net.peelo.kahvi.compiler.util.SourcePosition;

public abstract class BreakableStatement extends Statement
{
    public BreakableStatement(SourcePosition position)
    {
        super(position);
    }
}
