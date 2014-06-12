package net.peelo.kahvi.compiler.ast.declaration;

import net.peelo.kahvi.compiler.ast.Modifiers;
import net.peelo.kahvi.compiler.ast.Node;
import net.peelo.kahvi.compiler.ast.Scope;
import net.peelo.kahvi.compiler.util.SourcePosition;

import java.util.List;

public abstract class TypeDeclaration extends Node
    implements TypeBodyDeclaration
{
    private final Modifiers modifiers;
    private final String simpleName;
    private final List<TypeBodyDeclaration> members;
    private Scope enclosingScope;

    public TypeDeclaration(SourcePosition position,
                           Modifiers modifiers,
                           String simpleName,
                           List<TypeBodyDeclaration> members)
    {
        super(position);
        (this.modifiers = modifiers).setEnclosingScope(this);
        this.simpleName = simpleName;
        for (TypeBodyDeclaration tbd : (this.members = members))
        {
            tbd.setDeclaringType(this);
        }
    }

    public abstract <R, P> R accept(TypeDeclarationVisitor<R, P> visitor, P p);

    public final Modifiers getModifiers()
    {
        return this.modifiers;
    }

    /**
     * Returns simple name of the type declaration, or {@code null} if this
     * type is anonymous.
     */
    public final String getSimpleName()
    {
        return this.simpleName;
    }

    public final List<TypeBodyDeclaration> getMembers()
    {
        return this.members;
    }

    @Override
    public final Scope getEnclosingScope()
    {
        return this.enclosingScope;
    }

    public synchronized final void setEnclosingScope(Scope enclosingScope)
    {
        if (this.enclosingScope != null
            && this.enclosingScope != enclosingScope)
        {
            throw new IllegalStateException("Enclosing scope already set");
        }
        this.enclosingScope = enclosingScope;
    }
    
    @Override
    public final TypeDeclaration getDeclaringType()
    {
        if (this.enclosingScope instanceof TypeDeclaration)
        {
            return (TypeDeclaration) this.enclosingScope;
        } else {
            return null;
        }
    }

    @Override
    public final void setDeclaringType(TypeDeclaration declaringType)
    {
        this.setEnclosingScope(declaringType);
    }

    @Override
    public final ExecutableDeclaration getEnclosingExecutable()
    {
        Scope scope = this.enclosingScope;

        while (scope != null)
        {
            if (scope instanceof ExecutableDeclaration)
            {
                return (ExecutableDeclaration) scope;
            }
            scope = scope.getEnclosingScope();
        }

        return null;
    }

    @Override
    public final TypeDeclaration getEnclosingType()
    {
        Scope scope = this.enclosingScope;

        while (scope != null)
        {
            if (scope instanceof TypeDeclaration)
            {
                return (TypeDeclaration) scope;
            }
            scope = scope.getEnclosingScope();
        }

        return null;
    }
}
