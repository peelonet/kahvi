package net.peelo.kahvi.compiler.ast.type;

import net.peelo.kahvi.compiler.ast.Node;
import net.peelo.kahvi.compiler.ast.Scope;
import net.peelo.kahvi.compiler.util.SourcePosition;

public final class Wildcard extends Node implements TypeArgument
{
    private final Kind kind;
    private final ReferenceType bound;

    public enum Kind
    {
        UNBOUND,
        EXTENDS,
        SUPER;
    }

    public Wildcard(SourcePosition position)
    {
        this(position, Kind.UNBOUND, null);
    }

    public Wildcard(SourcePosition position, Kind kind, ReferenceType bound)
    {
        super(position);
        this.kind = kind;
        this.bound = bound;
    }

    public Kind getKind()
    {
        return this.kind;
    }

    public ReferenceType getBound()
    {
        return this.bound;
    }

    public void setEnclosingScope(Scope enclosingScope)
    {
        if (this.bound != null)
        {
            this.bound.setEnclosingScope(enclosingScope);
        }
    }

    @Override
    public <R, P> R accept(TypeArgumentVisitor<R, P> visitor, P p)
    {
        return visitor.visitWildcard(this, p);
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();

        sb.append('?');
        if (this.kind == Kind.EXTENDS)
        {
            sb.append(" extends ").append(this.bound);
        }
        else if (this.kind == Kind.SUPER)
        {
            sb.append(" super ").append(this.bound);
        }

        return sb.toString();
    }
}
