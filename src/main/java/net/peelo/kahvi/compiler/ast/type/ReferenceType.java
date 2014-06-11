package net.peelo.kahvi.compiler.ast.type;

import net.peelo.kahvi.compiler.util.SourcePosition;

public abstract class ReferenceType extends Type implements TypeArgument
{
    public ReferenceType(SourcePosition position)
    {
        super(position);
    }
}
