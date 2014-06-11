package net.peelo.kahvi.compiler.lookup.descriptor;

import java.util.List;

public final class MethodDescriptor
{
    /** Field descriptors of method parameters. */
    private final List<TypeDescriptor> parameterTypes;
    /** Field descriptor of return type. */
    private final TypeDescriptor returnType;

    public MethodDescriptor(List<TypeDescriptor> parameterTypes,
                            TypeDescriptor returnType)
    {
        this.parameterTypes = parameterTypes;
        this.returnType = returnType;
    }

    /**
     * Returns type descriptors of method parameters.
     */
    public List<TypeDescriptor> getParameterTypes()
    {
        return this.parameterTypes;
    }

    /**
     * Returns type descriptor of method return type.
     */
    public TypeDescriptor getReturnType()
    {
        return this.returnType;
    }

    public String getDescriptor()
    {
        StringBuilder sb = new StringBuilder();

        sb.append('(');
        for (TypeDescriptor td : this.parameterTypes)
        {
            sb.append(td.getDescriptor());
        }

        return sb.append(')').append(this.returnType.getDescriptor()).toString();
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        else if (o instanceof MethodDescriptor)
        {
            MethodDescriptor that = (MethodDescriptor) o;

            return this.returnType.equals(that.returnType)
                && this.parameterTypes.equals(that.parameterTypes);
        } else {
            return false;
        }
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();

        sb.append(this.returnType).append('(');
        for (int i = 0; i < this.parameterTypes.size(); ++i)
        {
            if (i > 0)
            {
                sb.append(',').append(' ');
            }
            sb.append(this.parameterTypes.get(i));
        }
        sb.append(')');

        return sb.toString();
    }
}
