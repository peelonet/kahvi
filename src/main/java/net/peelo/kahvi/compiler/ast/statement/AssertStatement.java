package net.peelo.kahvi.compiler.ast.statement;

import net.peelo.kahvi.compiler.ast.expression.Expression;
import net.peelo.kahvi.compiler.util.SourcePosition;

/**
 * Representation of an 'assert' statement.
 *
 * <p> For example:
 * <pre>
 *   assert <em>condition</em> ;
 *
 *   assert <em>condition</em> : <em>detail</em> ;
 * </pre>
 */
public final class AssertStatement extends Statement
{
    private final Expression condition;
    private final Expression detail;

    public AssertStatement(SourcePosition position, Expression condition)
    {
        this(position, condition, null);
    }

    public AssertStatement(SourcePosition position,
                           Expression condition,
                           Expression detail)
    {
        super(position);
        (this.condition = condition).setEnclosingScope(this);
        if ((this.detail = detail) != null)
        {
            this.detail.setEnclosingScope(this);
        }
    }

    public Expression getCondition()
    {
        return this.condition;
    }

    public Expression getDetail()
    {
        return this.detail;
    }

    @Override
    public <R, P> R accept(StatementVisitor<R, P> visitor, P p)
    {
        return visitor.visitAssertStatement(this, p);
    }

    @Override
    public String toString()
    {
        if (this.detail == null)
        {
            return "assert " + this.condition + ";";
        } else {
            return "assert " + this.condition + " : " + this.detail + ";";
        }
    }
}
