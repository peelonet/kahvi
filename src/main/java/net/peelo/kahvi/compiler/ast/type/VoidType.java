package net.peelo.kahvi.compiler.ast.type;

import net.peelo.kahvi.compiler.ast.AtomVisitor;
import net.peelo.kahvi.compiler.util.SourcePosition;

public final class VoidType extends Type
{
    public VoidType(SourcePosition position)
    {
        super(position);
    }

    @Override
    public <R, P> R accept(TypeVisitor<R, P> visitor, P p)
    {
        return visitor.visitVoidType(this, p);
    }

    @Override
    public <R, P> R accept(AtomVisitor<R, P> visitor, P p)
    {
        return visitor.visitVoidType(this, p);
    }

    @Override
    public String toString()
    {
        return "void";
    }
}
