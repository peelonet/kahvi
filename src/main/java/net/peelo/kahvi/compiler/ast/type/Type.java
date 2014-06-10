package net.peelo.kahvi.compiler.ast.type;

import net.peelo.kahvi.compiler.ast.Atom;

public abstract class Type extends Atom
{
    public Type(int lineNumber, int columnNumber)
    {
        super(lineNumber, columnNumber);
    }

    public abstract <R, P> R accept(TypeVisitor<R, P> visitor, P p);
}
