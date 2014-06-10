package net.peelo.kahvi.compiler.ast.statement;

import net.peelo.kahvi.compiler.ast.expression.Expression;

public final class AssertStatement extends Statement
{
    private final Expression condition;
    private final Expression detail;

    public AssertStatement(Expression condition,
                           int lineNumber,
                           int columnNumber)
    {
        this(condition, null, lineNumber, columnNumber);
    }

    public AssertStatement(Expression condition,
                           Expression detail,
                           int lineNumber,
                           int columnNumber)
    {
        super(lineNumber, columnNumber);
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
