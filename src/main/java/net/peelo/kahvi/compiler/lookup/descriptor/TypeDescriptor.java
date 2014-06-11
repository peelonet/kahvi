package net.peelo.kahvi.compiler.lookup.descriptor;

public abstract class TypeDescriptor implements Descriptor
{
    public static final TypeDescriptor parse(String source)
        throws DescriptorSyntaxException
    {
        return new DescriptorParser(source.toCharArray()).parseTypeDescriptor();
    }

    private ArrayTypeDescriptor arrayType;

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
