package net.peelo.kahvi.compiler.ast.declaration;

import net.peelo.kahvi.compiler.ast.Node;
import net.peelo.kahvi.compiler.ast.Scope;
import net.peelo.kahvi.compiler.ast.type.ReferenceType;
import net.peelo.kahvi.compiler.util.SourcePosition;

import java.util.Collections;
import java.util.List;

/**
 * Representation of a type parameter.
 *
 * <p> For example:
 * <pre>
 *   <em>name</em>
 *
 *   <em>name</em> extends <em>bounds</em>
 * </pre>
 */
public final class TypeParameterDeclaration extends Node
{
    private final String name;
    private final List<ReferenceType> bounds;

    public TypeParameterDeclaration(SourcePosition position, String name)
    {
        this(position, name, Collections.<ReferenceType>emptyList());
    }

    public TypeParameterDeclaration(SourcePosition position,
                                    String name,
                                    List<ReferenceType> bounds)
    {
        super(position);
        this.name = name;
        this.bounds = bounds;
    }

    public String getName()
    {
        return this.name;
    }

    public List<ReferenceType> getBounds()
    {
        return this.bounds;
    }

    public void setEnclosingScope(Scope enclosingScope)
    {
        for (ReferenceType rt : this.bounds)
        {
            rt.setEnclosingScope(enclosingScope);
        }
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();

        sb.append(this.name);
        if (!this.bounds.isEmpty())
        {
            boolean first = true;

            sb.append(" extends ");
            for (ReferenceType rt : this.bounds)
            {
                if (first)
                {
                    first = false;
                } else {
                    sb.append(" & ");
                }
                sb.append(rt);
            }
        }

        return sb.toString();
    }
}
