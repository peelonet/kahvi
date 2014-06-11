package net.peelo.kahvi.compiler.ast.statement;

import net.peelo.kahvi.compiler.ast.expression.Expression;
import net.peelo.kahvi.compiler.util.SourcePosition;

/**
 * Representation of a 'do' statement.
 *
 * <p> For example:
 * <pre>
 *   do
 *       <em>statement</em>
 *   while ( <em>expression</em> );
 * </pre>
 */
public final class DoWhileStatement extends ContinuableStatement
{
    private final Statement statement;
    private final Expression condition;

    public DoWhileStatement(SourcePosition position,
                            Statement statement,
                            Expression condition)
    {
        super(position);
        (this.statement = statement).setEnclosingScope(this);
        (this.condition = condition).setEnclosingScope(this);
    }

    public Statement getStatement()
    {
        return this.statement;
    }

    public Expression getCondition()
    {
        return this.condition;
    }

    @Override
    public <R, P> R accept(StatementVisitor<R, P> visitor, P p)
    {
        return visitor.visitDoWhileStatement(this, p);
    }

    @Override
    public String toString()
    {
        return "do " + this.statement + " while(" + this.condition + ");";
    }
}
