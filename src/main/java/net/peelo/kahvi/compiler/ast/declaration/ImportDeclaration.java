package net.peelo.kahvi.compiler.ast.declaration;

import net.peelo.kahvi.compiler.ast.Node;
import net.peelo.kahvi.compiler.util.Name;
import net.peelo.kahvi.compiler.util.SourcePosition;

public final class ImportDeclaration extends Node
{
    private final boolean _static;
    private final Name qualifiedIdentifier;
    private final boolean onDemand;

    public ImportDeclaration(SourcePosition position,
                             boolean _static,
                             Name qualifiedIdentifier,
                             boolean onDemand)
    {
        super(position);
        this._static = _static;
        this.qualifiedIdentifier = qualifiedIdentifier;
        this.onDemand = onDemand;
    }

    public boolean isStatic()
    {
        return this._static;
    }

    public Name getQualifiedIdentifier()
    {
        return this.qualifiedIdentifier;
    }

    public boolean isOnDemand()
    {
        return this.onDemand;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();

        sb.append("import ");
        if (this._static)
        {
            sb.append("static ");
        }
        sb.append(this.qualifiedIdentifier);
        if (this.onDemand)
        {
            sb.append(".*");
        }

        return sb.append(';').toString();
    }
}
