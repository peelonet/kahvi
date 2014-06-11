package net.peelo.kahvi.compiler.lookup.descriptor;

public abstract class TypeDescriptor
{
    private ArrayTypeDescriptor arrayType;

    public abstract String getDescriptor();

    public synchronized final ArrayTypeDescriptor getArrayType()
    {
        if (this.arrayType == null)
        {
            this.arrayType = new ArrayTypeDescriptor(this);
        }

        return this.arrayType;
    }

    public abstract <R, P> R accept(TypeDescriptorVisitor<R, P> visitor, P p);
}
