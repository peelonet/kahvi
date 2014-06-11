package net.peelo.kahvi.compiler.ast.expression;

import net.peelo.kahvi.compiler.ast.AtomVisitor;
import net.peelo.kahvi.compiler.ast.annotation.ElementValueVisitor;
import net.peelo.kahvi.compiler.ast.type.ReferenceType;
import net.peelo.kahvi.compiler.util.SourcePosition;

public final class InstanceOfExpression extends Expression
{
    private final Expression expression;
    private final ReferenceType type;

    public InstanceOfExpression(SourcePosition position,
                                Expression expression,
                                ReferenceType type)
    {
        super(position);
        (this.expression = expression).setEnclosingScope(this);
        (this.type = type).setEnclosingScope(this);
    }

    public Expression getExpression()
    {
        return this.expression;
    }

    public ReferenceType getType()
    {
        return this.type;
    }

    @Override
    public <R, P> R accept(ExpressionVisitor<R, P> visitor, P p)
    {
        return visitor.visitInstanceOfExpression(this, p);
    }

    @Override
    public <R, P> R accept(AtomVisitor<R, P> visitor, P p)
    {
        return visitor.visitInstanceOfExpression(this, p);
    }

    @Override
    public <R, P> R accept(ElementValueVisitor<R, P> visitor, P p)
    {
        return visitor.visitInstanceOfExpression(this, p);
    }

    @Override
    public String toString()
    {
        return this.expression + " instanceof " + this.type;
    }
}
