package net.peelo.kahvi.compiler.ast.expression;

import net.peelo.kahvi.compiler.ast.AtomVisitor;
import net.peelo.kahvi.compiler.ast.annotation.ElementValueVisitor;
import net.peelo.kahvi.compiler.util.SourcePosition;

public final class ConditionalExpression extends Expression
{
    private final Expression condition;
    private final Expression trueExpression;
    private final Expression falseExpression;

    public ConditionalExpression(SourcePosition position,
                                 Expression condition,
                                 Expression trueExpression,
                                 Expression falseExpression)
    {
        super(position);
        (this.condition = condition).setEnclosingScope(this);
        (this.trueExpression = trueExpression).setEnclosingScope(this);
        (this.falseExpression = falseExpression).setEnclosingScope(this);
    }

    public Expression getCondition()
    {
        return this.condition;
    }

    public Expression getTrueExpression()
    {
        return this.trueExpression;
    }

    public Expression getFalseExpression()
    {
        return this.falseExpression;
    }

    @Override
    public <R, P> R accept(ExpressionVisitor<R, P> visitor, P p)
    {
        return visitor.visitConditionalExpression(this, p);
    }

    @Override
    public <R, P> R accept(AtomVisitor<R, P> visitor, P p)
    {
        return visitor.visitConditionalExpression(this, p);
    }

    @Override
    public <R, P> R accept(ElementValueVisitor<R, P> visitor, P p)
    {
        return visitor.visitConditionalExpression(this, p);
    }

    @Override
    public String toString()
    {
        return this.condition + " ? ... : ...";
    }
}
