package net.peelo.kahvi.compiler.ast.expression;

import net.peelo.kahvi.compiler.ast.Node;

public abstract class Expression extends Node
{
    public Expression(int lineNumber, int columnNumber)
    {
        super(lineNumber, columnNumber);
    }

    public abstract <R, P> R accept(ExpressionVisitor<R, P> visitor, P p);
}
