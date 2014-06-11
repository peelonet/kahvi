package net.peelo.kahvi.compiler.ast.statement;

import net.peelo.kahvi.compiler.ast.expression.Expression;
import net.peelo.kahvi.compiler.util.SourcePosition;

/**
 * Representation of a 'return' statement.
 *
 * <p> For example:
 * <pre>
 *   return;
 *
 *   return <em>expression</em> ;
 * </pre>
 */
public final class ReturnStatement extends Statement
{
    private final Expression expression;

    public ReturnStatement(SourcePosition position)
    {
        this(position, null);
    }

    public ReturnStatement(SourcePosition position, Expression expression)
    {
        super(position);
        if ((this.expression = expression) != null)
        {
            this.expression.setEnclosingScope(this);
        }
    }

    public Expression getExpression()
    {
        return this.expression;
    }

    @Override
    public <R, P> R accept(StatementVisitor<R, P> visitor, P p)
    {
        return visitor.visitReturnStatement(this, p);
    }

    @Override
    public String toString()
    {
        if (this.expression == null)
        {
            return "return;";
        } else {
            return "return " + this.expression + ";";
        }
    }
}
