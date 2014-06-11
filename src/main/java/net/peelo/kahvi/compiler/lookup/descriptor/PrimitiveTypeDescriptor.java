package net.peelo.kahvi.compiler.lookup.descriptor;

import net.peelo.kahvi.compiler.lookup.Primitive;

public final class PrimitiveTypeDescriptor extends TypeDescriptor
{
    private final Primitive kind;

    public PrimitiveTypeDescriptor(Primitive kind)
    {
        if (kind == null)
        {
            throw new NullPointerException("kind");
        }
        this.kind = kind;
    }

    public Primitive getKind()
    {
        return this.kind;
    }

    @Override
    public String getDescriptor()
    {
        switch (this.kind)
        {
            case BYTE:
                return "B";
            case CHAR:
                return "C";
            case SHORT:
                return "S";
            case INT:
                return "I";
            case LONG:
                return "J";
            case FLOAT:
                return "F";
            case DOUBLE:
                return "D";
            case BOOLEAN:
                return "Z";
        }

        throw new IllegalStateException("Unknown primitive type");
    }

    @Override
    public <R, P> R accept(TypeDescriptorVisitor<R, P> visitor, P p)
    {
        return visitor.visitPrimitiveTypeDescriptor(this, p);
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        else if (o instanceof PrimitiveTypeDescriptor)
        {
            PrimitiveTypeDescriptor that = (PrimitiveTypeDescriptor) o;

            return this.kind == that.kind;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode()
    {
        return this.getDescriptor().hashCode();
    }

    @Override
    public String toString()
    {
        return this.kind.toString();
    }
}
