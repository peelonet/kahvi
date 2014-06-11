package net.peelo.kahvi.compiler.ast.expression;

import net.peelo.kahvi.compiler.ast.Atom;
import net.peelo.kahvi.compiler.ast.annotation.ElementValue;

public abstract class Expression extends Atom implements ElementValue
{
    public Expression(int lineNumber, int columnNumber)
    {
        super(lineNumber, columnNumber);
    }

    public abstract <R, P> R accept(ExpressionVisitor<R, P> visitor, P p);
}
