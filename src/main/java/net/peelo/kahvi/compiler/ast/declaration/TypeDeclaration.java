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
    private final String name;
    private final List<TypeBodyDeclaration> declarations;
    private Scope enclosingScope;

    public TypeDeclaration(SourcePosition position,
                           Modifiers modifiers,
                           String name,
                           List<TypeBodyDeclaration> declarations)
    {
        super(position);
        (this.modifiers = modifiers).setEnclosingScope(this);
        this.name = name;
        for (TypeBodyDeclaration tbd : (this.declarations = declarations))
        {
            tbd.setDeclaringType(this);
        }
    }

    public final Modifiers getModifiers()
    {
        return this.modifiers;
    }

    /**
     * Returns simple name of the type declaration, or {@code null} if this
     * type is anonymous.
     */
    public final String getName()
    {
        return this.name;
    }

    public final List<TypeBodyDeclaration> getDeclarations()
    {
        return this.declarations;
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
}
