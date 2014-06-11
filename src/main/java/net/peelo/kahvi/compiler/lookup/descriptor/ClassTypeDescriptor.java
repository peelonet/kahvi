package net.peelo.kahvi.compiler.lookup.descriptor;

import net.peelo.kahvi.compiler.util.Name;

public final class ClassTypeDescriptor extends ReferenceTypeDescriptor
{
    /** Name of the class. */
    private final Name className;

    public ClassTypeDescriptor(Name className)
    {
        if (className == null)
        {
            throw new NullPointerException("className");
        }
        this.className = className;
    }

    /**
     * Returns full name of the class.
     */
    public Name getClassName()
    {
        return this.className;
    }

    public String getSimpleName()
    {
        String name = this.className.getSimpleName();
        int index = name.lastIndexOf('$');

        return index >= 0 ? name.substring(index + 1) : name;
    }

    /**
     * Returns name of the package where the class is declared, or {@code null}
     * if the class is declared in the default package.
     */
    public Name getPackageName()
    {
        return this.className.getQualifier();
    }

    @Override
    public String getDescriptor()
    {
        StringBuilder sb = new StringBuilder();
        boolean first = true;

        sb.append('L');
        for (String part : this.className)
        {
            if (first)
            {
                first = false;
            } else {
                sb.append('/');
            }
            sb.append(part);
        }
        
        return sb.append(';').toString();
    }

    @Override
    public <R, P> R accept(TypeDescriptorVisitor<R, P> visitor, P p)
    {
        return visitor.visitClassTypeDescriptor(this, p);
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        else if (o instanceof ClassTypeDescriptor)
        {
            ClassTypeDescriptor that = (ClassTypeDescriptor) o;

            return this.className.equals(that.className);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode()
    {
        return this.className.hashCode();
    }

    @Override
    public String toString()
    {
        return this.className.toString();
    }
}
