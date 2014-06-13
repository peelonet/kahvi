package net.peelo.kahvi.compiler.ast.expression;

import net.peelo.kahvi.compiler.util.SourcePosition;

public abstract class LiteralExpression extends Expression
{
    public LiteralExpression(SourcePosition position)
    {
        super(position);
    }
}
