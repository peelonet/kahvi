package net.peelo.kahvi.compiler.ast.expression;

import net.peelo.kahvi.compiler.ast.Atom;
import net.peelo.kahvi.compiler.ast.annotation.ElementValue;
import net.peelo.kahvi.compiler.util.SourcePosition;

public abstract class Expression extends Atom
    implements ElementValue, VariableInitializer
{
    public Expression(SourcePosition position)
    {
        super(position);
    }

    public abstract <R, P> R accept(ExpressionVisitor<R, P> visitor, P p);
}
