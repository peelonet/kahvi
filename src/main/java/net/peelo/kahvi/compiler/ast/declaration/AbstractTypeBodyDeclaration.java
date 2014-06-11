package net.peelo.kahvi.compiler.ast.declaration;

import net.peelo.kahvi.compiler.ast.Modifiers;
import net.peelo.kahvi.compiler.ast.Node;
import net.peelo.kahvi.compiler.ast.Scope;
import net.peelo.kahvi.compiler.util.SourcePosition;

public abstract class AbstractTypeBodyDeclaration extends Node
    implements TypeBodyDeclaration
{
    private final Modifiers modifiers;
    private TypeDeclaration declaringType;

    public AbstractTypeBodyDeclaration(SourcePosition position,
                                       Modifiers modifiers)
    {
        super(position);
        (this.modifiers = modifiers).setEnclosingScope(this);
    }

    public final Modifiers getModifiers()
    {
        return this.modifiers;
    }

    @Override
    public final boolean isStatic()
    {
        return this.modifiers.isStatic();
    }

    
    @Override
    public final TypeDeclaration getDeclaringType()
    {
        return this.declaringType;
    }

    @Override
    public synchronized final void setDeclaringType(TypeDeclaration declaringType)
    {
        if (this.declaringType != null
            && this.declaringType != declaringType)
        {
            throw new IllegalStateException("Declaring type already set");
        }
        this.declaringType = declaringType;
    }

    @Override
    public final Scope getEnclosingScope()
    {
        return this.declaringType;
    }

    @Override
    public final TypeDeclaration getEnclosingType()
    {
        return this.declaringType;
    }

    @Override
    public final ExecutableDeclaration getEnclosingExecutable()
    {
        Scope scope = this.declaringType;

        while (scope != null && !(scope instanceof ExecutableDeclaration))
        {
            scope = scope.getEnclosingScope();
        }
        if (scope instanceof ExecutableDeclaration)
        {
            return (ExecutableDeclaration) scope;
        } else {
            return null;
        }
    }
}
