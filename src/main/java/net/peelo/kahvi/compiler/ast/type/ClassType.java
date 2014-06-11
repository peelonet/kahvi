package net.peelo.kahvi.compiler.ast.type;

import net.peelo.kahvi.compiler.ast.AtomVisitor;
import net.peelo.kahvi.compiler.util.Name;
import net.peelo.kahvi.compiler.util.SourcePosition;

import java.util.Collections;
import java.util.List;

public final class ClassType extends ReferenceType
{
    /** Qualified name of the type. */
    private final Name className;
    /** Optional type arguments. */
    private final List<TypeArgument> typeArguments;

    public ClassType(SourcePosition position, Name className)
    {
        this(position, className, Collections.<TypeArgument>emptyList());
    }

    public ClassType(SourcePosition position,
                     Name className,
                     List<TypeArgument> typeArguments)
    {
        super(position);
        this.className = className;
        for (TypeArgument ta : (this.typeArguments = typeArguments))
        {
            ta.setEnclosingScope(this);
        }
    }

    /**
     * Returns qualified name of the class.
     */
    public Name getClassName()
    {
        return this.className;
    }

    /**
     * Returns list of optional type arguments.
     */
    public List<TypeArgument> getTypeArguments()
    {
        return this.typeArguments;
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
        StringBuilder sb = new StringBuilder();

        sb.append(this.className);
        if (!this.typeArguments.isEmpty())
        {
            boolean first = true;

            sb.append('<');
            for (TypeArgument ta : this.typeArguments)
            {
                if (first)
                {
                    first = false;
                } else {
                    sb.append(", ");
                }
                sb.append(ta);
            }
            sb.append('>');
        }

        return sb.toString();
    }
}
