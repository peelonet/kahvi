package net.peelo.kahvi.compiler.ast.statement;

import net.peelo.kahvi.compiler.ast.expression.Expression;

public final class SynchronizedStatement extends Statement
{
    private final Expression expression;
    private final BlockStatement block;

    public SynchronizedStatement(Expression expression,
                                 BlockStatement block,
                                 int lineNumber,
                                 int columnNumber)
    {
        super(lineNumber, columnNumber);
        (this.expression = expression).setEnclosingScope(this);
        (this.block = block).setEnclosingScope(this);
    }

    public Expression getExpression()
    {
        return this.expression;
    }

    public BlockStatement getBlock()
    {
        return this.block;
    }

    @Override
    public <R, P> R accept(StatementVisitor<R, P> visitor, P p)
    {
        return visitor.visitSynchronizedStatement(this, p);
    }

    @Override
    public String toString()
    {
        return "synchronized (" + this.expression + ") {...}";
    }
}
