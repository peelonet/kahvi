package net.peelo.kahvi.compiler.ast.expression;

public abstract class AssignableExpression extends Expression
{
    public AssignableExpression(int lineNumber, int columnNumber)
    {
        super(lineNumber, columnNumber);
    }

    public abstract <R, P> R accept(AssignableExpressionVisitor<R, P> visitor, P p);
}
