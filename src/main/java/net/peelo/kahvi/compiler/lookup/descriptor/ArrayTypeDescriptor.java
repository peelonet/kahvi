package net.peelo.kahvi.compiler.lookup.descriptor;

public final class ArrayTypeDescriptor extends ReferenceTypeDescriptor
{
    private final TypeDescriptor componentType;

    ArrayTypeDescriptor(TypeDescriptor componentType)
    {
        this.componentType = componentType;
    }

    public TypeDescriptor getComponentType()
    {
        return this.componentType;
    }

    @Override
    public String getDescriptor()
    {
        return "[".concat(this.componentType.getDescriptor());
    }

    @Override
    public <R, P> R accept(TypeDescriptorVisitor<R, P> visitor, P p)
    {
        return visitor.visitArrayTypeDescriptor(this, p);
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        else if (o instanceof ArrayTypeDescriptor)
        {
            ArrayTypeDescriptor that = (ArrayTypeDescriptor) o;

            return this.componentType.equals(that.componentType);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode()
    {
        return this.componentType.hashCode() + 5;
    }

    @Override
    public String toString()
    {
        return this.componentType + "[]";
    }
}
