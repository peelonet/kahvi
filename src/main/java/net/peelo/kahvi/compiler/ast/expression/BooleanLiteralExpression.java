package net.peelo.kahvi.compiler.ast.expression;

import net.peelo.kahvi.compiler.ast.AtomVisitor;
import net.peelo.kahvi.compiler.ast.annotation.ElementValueVisitor;
import net.peelo.kahvi.compiler.util.SourcePosition;

public final class BooleanLiteralExpression extends LiteralExpression
{
    private final boolean value;

    public BooleanLiteralExpression(SourcePosition position, boolean value)
    {
        super(position);
        this.value = value;
    }

    public boolean getValue()
    {
        return this.value;
    }

    @Override
    public <R, P> R accept(ExpressionVisitor<R, P> visitor, P p)
    {
        return visitor.visitBooleanLiteralExpression(this, p);
    }

    @Override
    public <R, P> R accept(AtomVisitor<R, P> visitor, P p)
    {
        return visitor.visitBooleanLiteralExpression(this, p);
    }

    @Override
    public <R, P> R accept(ElementValueVisitor<R, P> visitor, P p)
    {
        return visitor.visitBooleanLiteralExpression(this, p);
    }

    @Override
    public <R, P> R accept(VariableInitializerVisitor<R, P> visitor, P p)
    {
        return visitor.visitBooleanLiteralExpression(this, p);
    }

    @Override
    public String toString()
    {
        return this.value ? "true" : "false";
    }
}
