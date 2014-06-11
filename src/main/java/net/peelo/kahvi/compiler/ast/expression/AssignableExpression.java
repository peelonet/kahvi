package net.peelo.kahvi.compiler.ast.expression;

import net.peelo.kahvi.compiler.util.SourcePosition;

public abstract class AssignableExpression extends Expression
{
    public AssignableExpression(SourcePosition position)
    {
        super(position);
    }

    public abstract <R, P> R accept(AssignableExpressionVisitor<R, P> visitor, P p);
}
