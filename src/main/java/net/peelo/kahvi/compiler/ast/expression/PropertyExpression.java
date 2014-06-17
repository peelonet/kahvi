package net.peelo.kahvi.compiler.ast.expression;

import net.peelo.kahvi.compiler.ast.AtomVisitor;
import net.peelo.kahvi.compiler.ast.annotation.ElementValueVisitor;
import net.peelo.kahvi.compiler.util.SourcePosition;

/**
 * AST node for property access expression.
 *
 * <p> For example:
 * <pre>
 *   {@literal @} <em>identifier</em>
 *   <em>expression</em> {@literal .@} <em>identifier</em>
 * </pre>
 */
public final class PropertyExpression extends AssignableExpression
{
    private final Expression expression;
    private final String identifier;

    public PropertyExpression(SourcePosition position, String identifier)
    {
        this(position, null, identifier);
    }

    public PropertyExpression(SourcePosition position,
                              Expression expression,
                              String identifier)
    {
        super(position);
        if ((this.expression = expression) != null)
        {
            this.expression.setEnclosingScope(this);
        }
        this.identifier = identifier;
    }

    public Expression getExpression()
    {
        return this.expression;
    }

    public String getIdentifier()
    {
        return this.identifier;
    }

    @Override
    public <R, P> R accept(ExpressionVisitor<R, P> visitor, P p)
    {
        return visitor.visitPropertyExpression(this, p);
    }

    @Override
    public <R, P> R accept(AssignableExpressionVisitor<R, P> visitor, P p)
    {
        return visitor.visitPropertyExpression(this, p);
    }

    @Override
    public <R, P> R accept(AtomVisitor<R, P> visitor, P p)
    {
        return visitor.visitPropertyExpression(this, p);
    }

    @Override
    public <R, P> R accept(VariableInitializerVisitor<R, P> visitor, P p)
    {
        return visitor.visitPropertyExpression(this, p);
    }

    @Override
    public <R, P> R accept(ElementValueVisitor<R, P> visitor, P p)
    {
        return visitor.visitPropertyExpression(this, p);
    }

    @Override
    public String toString()
    {
        if (this.expression == null)
        {
            return "@" + this.identifier;
        } else {
            return this.expression + ".@" + this.identifier;
        }
    }
}
