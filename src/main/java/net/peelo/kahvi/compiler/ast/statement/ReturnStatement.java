package net.peelo.kahvi.compiler.ast.statement;

import net.peelo.kahvi.compiler.ast.expression.Expression;

public final class ReturnStatement extends Statement
{
    private final Expression expression;

    public ReturnStatement(int lineNumber, int columnNumber)
    {
        this(null, lineNumber, columnNumber);
    }

    public ReturnStatement(Expression expression,
                           int lineNumber,
                           int columnNumber)
    {
        super(lineNumber, columnNumber);
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
