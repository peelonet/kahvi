package net.peelo.kahvi.compiler.ast.statement;

import net.peelo.kahvi.compiler.ast.expression.Expression;
import net.peelo.kahvi.compiler.util.SourcePosition;

/**
 * Representation of a 'while' loop statement.
 *
 * <p> For example:
 * <pre>
 *   while ( <em>condition</em> )
 *       <em>statement</em>
 * </pre>
 */
public final class WhileStatement extends ContinuableStatement
{
    private final Expression condition;
    private final Statement statement;

    public WhileStatement(SourcePosition position,
                          Expression condition,
                          Statement statement)
    {
        super(position);
        (this.condition = condition).setEnclosingScope(this);
        (this.statement = statement).setEnclosingScope(this);
    }

    public Expression getCondition()
    {
        return this.condition;
    }

    public Statement getStatement()
    {
        return this.statement;
    }

    @Override
    public <R, P> R accept(StatementVisitor<R, P> visitor, P p)
    {
        return visitor.visitWhileStatement(this, p);
    }

    @Override
    public String toString()
    {
        return "while (" + this.condition + ") " + this.statement;
    }
}
