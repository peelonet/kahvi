package net.peelo.kahvi.compiler.ast.type;

import net.peelo.kahvi.compiler.ast.AtomVisitor;
import net.peelo.kahvi.compiler.util.SourcePosition;

/**
 * Representation of an array type.
 *
 * <p> For example:
 * <pre>
 *   <em>componentType</em> []
 * </pre>
 */
public final class ArrayType extends ReferenceType
{
    private final Type componentType;

    public ArrayType(SourcePosition position, Type componentType)
    {
        super(position);
        (this.componentType = componentType).setEnclosingScope(this);
    }

    public Type getComponentType()
    {
        return this.componentType;
    }

    @Override
    public <R, P> R accept(TypeVisitor<R, P> visitor, P p)
    {
        return visitor.visitArrayType(this, p);
    }

    @Override
    public <R, P> R accept(TypeArgumentVisitor<R, P> visitor, P p)
    {
        return visitor.visitArrayType(this, p);
    }

    @Override
    public <R, P> R accept(AtomVisitor<R, P> visitor, P p)
    {
        return visitor.visitArrayType(this, p);
    }

    @Override
    public String toString()
    {
        return this.componentType + "[]";
    }
}
