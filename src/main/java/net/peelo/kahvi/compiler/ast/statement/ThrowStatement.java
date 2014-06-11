package net.peelo.kahvi.compiler.ast.statement;

import net.peelo.kahvi.compiler.ast.expression.Expression;
import net.peelo.kahvi.compiler.util.SourcePosition;

/**
 * Representation of a 'throw' statement.
 *
 * <p> For example:
 * <pre>
 *   throw <em>expression</em> ;
 * </pre>
 */
public final class ThrowStatement extends Statement
{
    private final Expression expression;

    public ThrowStatement(SourcePosition position, Expression expression)
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
        return visitor.visitThrowStatement(this, p);
    }

    @Override
    public String toString()
    {
        return "throw " + this.expression + ";";
    }
}
