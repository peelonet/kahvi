package net.peelo.kahvi.compiler.ast.type;

import net.peelo.kahvi.compiler.ast.Atom;
import net.peelo.kahvi.compiler.util.SourcePosition;

public abstract class Type extends Atom
{
    public Type(SourcePosition position)
    {
        super(position);
    }

    public abstract <R, P> R accept(TypeVisitor<R, P> visitor, P p);
}
