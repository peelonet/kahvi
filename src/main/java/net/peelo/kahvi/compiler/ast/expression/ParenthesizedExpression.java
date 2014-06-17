package net.peelo.kahvi.compiler.ast.expression;

import net.peelo.kahvi.compiler.ast.AtomVisitor;
import net.peelo.kahvi.compiler.ast.annotation.ElementValueVisitor;
import net.peelo.kahvi.compiler.util.SourcePosition;

/**
 * AST node for an parenthesized expression.
 * 
 * <p> For example:
 * <pre>
 *   ( <em>expression</em> )
 * </pre>
 */
public final class ParenthesizedExpression extends AssignableExpression
{
    private final Expression expression;

    public ParenthesizedExpression(SourcePosition position,
                                   Expression expression)
    {
        super(position);
        (this.expression = expression).setEnclosingScope(this);
    }

    public Expression getExpression()
    {
        return this.expression;
    }

    @Override
    public <R, P> R accept(ExpressionVisitor<R, P> visitor, P p)
    {
        return visitor.visitParenthesizedExpression(this, p);
    }

    @Override
    public <R, P> R accept(AssignableExpressionVisitor<R, P> visitor, P p)
    {
        return visitor.visitParenthesizedExpression(this, p);
    }

    @Override
    public <R, P> R accept(AtomVisitor<R, P> visitor, P p)
    {
        return visitor.visitParenthesizedExpression(this, p);
    }

    @Override
    public <R, P> R accept(VariableInitializerVisitor<R, P> visitor, P p)
    {
        return visitor.visitParenthesizedExpression(this, p);
    }

    @Override
    public <R, P> R accept(ElementValueVisitor<R, P> visitor, P p)
    {
        return visitor.visitParenthesizedExpression(this, p);
    }

    @Override
    public String toString()
    {
        return "(" + this.expression + ")";
    }
}
