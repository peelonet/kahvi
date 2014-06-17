package net.peelo.kahvi.compiler.ast.expression;

import net.peelo.kahvi.compiler.ast.AtomVisitor;
import net.peelo.kahvi.compiler.ast.annotation.ElementValueVisitor;
import net.peelo.kahvi.compiler.util.SourcePosition;

/**
 * AST node for an array access expression.
 *
 * <p> For example:
 * <pre>
 *   <em>expression</em> [ <em>index</em> ]
 * </pre>
 */
public final class ArrayAccessExpression extends AssignableExpression
{
    private final Expression expression;
    private final Expression index;

    public ArrayAccessExpression(SourcePosition position,
                                 Expression expression,
                                 Expression index)
    {
        super(position);
        (this.expression = expression).setEnclosingScope(this);
        (this.index = index).setEnclosingScope(this);
    }

    public Expression getExpression()
    {
        return this.expression;
    }

    public Expression getIndex()
    {
        return this.index;
    }

    @Override
    public <R, P> R accept(ExpressionVisitor<R, P> visitor, P p)
    {
        return visitor.visitArrayAccessExpression(this, p);
    }

    @Override
    public <R, P> R accept(AssignableExpressionVisitor<R, P> visitor, P p)
    {
        return visitor.visitArrayAccessExpression(this, p);
    }

    @Override
    public <R, P> R accept(AtomVisitor<R, P> visitor, P p)
    {
        return visitor.visitArrayAccessExpression(this, p);
    }

    @Override
    public <R, P> R accept(VariableInitializerVisitor<R, P> visitor, P p)
    {
        return visitor.visitArrayAccessExpression(this, p);
    }

    @Override
    public <R, P> R accept(ElementValueVisitor<R, P> visitor, P p)
    {
        return visitor.visitArrayAccessExpression(this, p);
    }

    @Override
    public String toString()
    {
        return this.expression + "[" + this.index + "]";
    }
}
