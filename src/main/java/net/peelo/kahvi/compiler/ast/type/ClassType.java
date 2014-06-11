package net.peelo.kahvi.compiler.ast.type;

import net.peelo.kahvi.compiler.ast.AtomVisitor;
import net.peelo.kahvi.compiler.util.Name;
import net.peelo.kahvi.compiler.util.SourcePosition;

public final class ClassType extends ReferenceType
{
    private final Name className;

    public ClassType(SourcePosition position, Name className)
    {
        super(position);
        this.className = className;
    }

    public Name getClassName()
    {
        return this.className;
    }

    @Override
    public <R, P> R accept(TypeVisitor<R, P> visitor, P p)
    {
        return visitor.visitClassType(this, p);
    }

    @Override
    public <R, P> R accept(TypeArgumentVisitor<R, P> visitor, P p)
    {
        return visitor.visitClassType(this, p);
    }

    @Override
    public <R, P> R accept(AtomVisitor<R, P> visitor, P p)
    {
        return visitor.visitClassType(this, p);
    }

    @Override
    public String toString()
    {
        return this.className.toString();
    }
}
