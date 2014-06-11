package net.peelo.kahvi.compiler.ast.statement;

import net.peelo.kahvi.compiler.ast.expression.Expression;

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

    public ThrowStatement(Expression expression,
                          int lineNumber,
                          int columnNumber)
    {
        super(lineNumber, columnNumber);
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
