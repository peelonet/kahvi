package net.peelo.kahvi.compiler.ast.statement;

import net.peelo.kahvi.compiler.ast.expression.Expression;

import java.util.List;

/**
 * Representation of 'for' loop statement.
 *
 * <p> For example:
 * <pre>
 *   for ( <em>initializer</em> ; <em>condition</em> ; <em>update</em> )
 *       <em>statement</em>
 * </pre>
 */
public final class ForStatement extends Statement
{
    private final List<Statement> initializer;
    private final Expression condition;
    private final List<ExpressionStatement> update;
    private final Statement statement;

    public ForStatement(List<Statement> initializer,
                        Expression condition,
                        List<ExpressionStatement> update,
                        Statement statement,
                        int lineNumber,
                        int columnNumber)
    {
        super(lineNumber, columnNumber);
        for (Statement s : (this.initializer = initializer))
        {
            s.setEnclosingScope(this);
        }
        if ((this.condition = condition) != null)
        {
            this.condition.setEnclosingScope(this);
        }
        for (Statement s : (this.update = update))
        {
            s.setEnclosingScope(this);
        }
        (this.statement = statement).setEnclosingScope(this);
    }

    public List<Statement> getInitializer()
    {
        return this.initializer;
    }

    public Expression getCondition()
    {
        return this.condition;
    }

    public List<ExpressionStatement> getUpdate()
    {
        return this.update;
    }

    /**
     * Returns body of the loop.
     */
    public Statement getStatement()
    {
        return this.statement;
    }

    @Override
    public <R, P> R accept(StatementVisitor<R, P> visitor, P p)
    {
        return visitor.visitForStatement(this, p);
    }

    @Override
    public String toString()
    {
        return "for (...; ...; ...) " + this.statement;
    }
}
