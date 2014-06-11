package net.peelo.kahvi.compiler.lookup.descriptor;

public final class VoidTypeDescriptor extends TypeDescriptor
{
    @Override
    public String getDescriptor()
    {
        return "V";
    }

    @Override
    public <R, P> R accept(TypeDescriptorVisitor<R, P> visitor, P p)
    {
        return visitor.visitVoidTypeDescriptor(this, p);
    }

    @Override
    public boolean equals(Object o)
    {
        return o instanceof VoidTypeDescriptor;
    }

    @Override
    public int hashCode()
    {
        return 0;
    }

    @Override
    public String toString()
    {
        return "void";
    }
}
