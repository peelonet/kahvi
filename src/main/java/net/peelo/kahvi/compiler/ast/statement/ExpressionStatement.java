package net.peelo.kahvi.compiler.ast.statement;

import net.peelo.kahvi.compiler.ast.expression.Expression;
import net.peelo.kahvi.compiler.util.SourcePosition;

/**
 * Representation of an expression statement.
 *
 * <p> For example:
 * <pre>
 *   <em>expression</em> ;
 * </pre>
 */
public final class ExpressionStatement extends Statement
{
    private final Expression expression;

    public ExpressionStatement(SourcePosition position, Expression expression)
    {
        super(position);
        (this.expression = expression).setEnclosingScope(this);
    }

    public Expression getExpression()
    {
        return this.expression;
    }

    @Override
    public <R, P> R accept(StatementVisitor<R, P> visitor, P p)
    {
        return visitor.visitExpressionStatement(this, p);
    }

    @Override
    public String toString()
    {
        return this.expression + ";";
    }
}
