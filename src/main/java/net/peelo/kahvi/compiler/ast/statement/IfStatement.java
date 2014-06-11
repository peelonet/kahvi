package net.peelo.kahvi.compiler.ast.statement;

import net.peelo.kahvi.compiler.ast.expression.Expression;
import net.peelo.kahvi.compiler.util.SourcePosition;

/**
 * Representation of an 'if' statement.
 *
 * <p> For example:
 * <pre>
 *   if ( <em>condition</em> )
 *       <em>thenStatement</em>
 *
 *   if ( <em>condition</em> )
 *       <em>thenStatement</em>
 *   else
 *       <em>elseStatement</em>
 * </pre>
 */
public final class IfStatement extends Statement
{
    private final Expression condition;
    private final Statement thenStatement;
    private final Statement elseStatement;

    public IfStatement(SourcePosition position,
                       Expression condition,
                       Statement thenStatement)
    {
        this(position, condition, thenStatement, null);
    }

    public IfStatement(SourcePosition position,
                       Expression condition,
                       Statement thenStatement,
                       Statement elseStatement)
    {
        super(position);
        (this.condition = condition).setEnclosingScope(this);
        (this.thenStatement = thenStatement).setEnclosingScope(this);
        if ((this.elseStatement = elseStatement) != null)
        {
            this.elseStatement.setEnclosingScope(this);
        }
    }

    public Expression getCondition()
    {
        return this.condition;
    }

    public Statement getThenStatement()
    {
        return this.thenStatement;
    }

    public Statement getElseStatement()
    {
        return this.elseStatement;
    }

    @Override
    public <R, P> R accept(StatementVisitor<R, P> visitor, P p)
    {
        return visitor.visitIfStatement(this, p);
    }

    @Override
    public String toString()
    {
        if (this.elseStatement == null)
        {
            return "if (" + this.condition + ") ...";
        } else {
            return "if (" + this.condition + ") ... else ...";
        }
    }
}
