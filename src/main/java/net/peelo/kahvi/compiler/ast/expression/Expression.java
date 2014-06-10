package net.peelo.kahvi.compiler.ast.expression;

import net.peelo.kahvi.compiler.ast.Atom;

public abstract class Expression extends Atom
{
    public Expression(int lineNumber, int columnNumber)
    {
        super(lineNumber, columnNumber);
    }

    public abstract <R, P> R accept(ExpressionVisitor<R, P> visitor, P p);
}
