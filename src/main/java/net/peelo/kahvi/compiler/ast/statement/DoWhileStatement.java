package net.peelo.kahvi.compiler.ast.statement;

import net.peelo.kahvi.compiler.ast.expression.Expression;

public final class DoWhileStatement extends Statement
{
    private final Statement statement;
    private final Expression condition;

    public DoWhileStatement(Statement statement,
                            Expression condition,
                            int lineNumber,
                            int columnNumber)
    {
        super(lineNumber, columnNumber);
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