package net.peelo.kahvi.compiler.ast.type;

import net.peelo.kahvi.compiler.ast.AtomVisitor;
import net.peelo.kahvi.compiler.lookup.Primitive;
import net.peelo.kahvi.compiler.util.SourcePosition;

public final class PrimitiveType extends Type
{
    private final Primitive kind;

    public PrimitiveType(SourcePosition position, Primitive kind)
    {
        super(position);
        this.kind = kind;
    }

    public Primitive getKind()
    {
        return this.kind;
    }

    @Override
    public <R, P> R accept(TypeVisitor<R, P> visitor, P p)
    {
        return visitor.visitPrimitiveType(this, p);
    }

    @Override
    public <R, P> R accept(AtomVisitor<R, P> visitor, P p)
    {
        return visitor.visitPrimitiveType(this, p);
    }

    @Override
    public String toString()
    {
        return this.kind.toString();
    }
}
