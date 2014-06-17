package net.peelo.kahvi.compiler.ast.expression;

import net.peelo.kahvi.compiler.ast.AtomVisitor;
import net.peelo.kahvi.compiler.ast.annotation.ElementValueVisitor;
import net.peelo.kahvi.compiler.util.SourcePosition;

public final class IdentifierExpression extends AssignableExpression
{
    private final String name;

    public IdentifierExpression(SourcePosition position, String name)
    {
        super(position);
        this.name = name;
    }

    public String getName()
    {
        return this.name;
    }

    @Override
    public <R, P> R accept(ExpressionVisitor<R, P> visitor, P p)
    {
        return visitor.visitIdentifierExpression(this, p);
    }

    @Override
    public <R, P> R accept(AssignableExpressionVisitor<R, P> visitor, P p)
    {
        return visitor.visitIdentifierExpression(this, p);
    }

    @Override
    public <R, P> R accept(AtomVisitor<R, P> visitor, P p)
    {
        return visitor.visitIdentifierExpression(this, p);
    }

    @Override
    public <R, P> R accept(VariableInitializerVisitor<R, P> visitor, P p)
    {
        return visitor.visitIdentifierExpression(this, p);
    }

    @Override
    public <R, P> R accept(ElementValueVisitor<R, P> visitor, P p)
    {
        return visitor.visitIdentifierExpression(this, p);
    }

    @Override
    public String toString()
    {
        return this.name;
    }
}
