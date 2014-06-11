package net.peelo.kahvi.compiler.ast.type;

import net.peelo.kahvi.compiler.ast.AtomVisitor;

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

    public ArrayType(Type componentType,
                     int lineNumber,
                     int columnNumber)
    {
        super(lineNumber, columnNumber);
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
