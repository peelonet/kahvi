package net.peelo.kahvi.compiler.ast;

import net.peelo.kahvi.compiler.ast.declaration.ExecutableDeclaration;
import net.peelo.kahvi.compiler.ast.declaration.TypeDeclaration;

/**
 * Common super class for expressions and types.
 */
public abstract class Atom extends Node implements Scope
{
    private Scope enclosingScope;

    public Atom(int lineNumber, int columnNumber)
    {
        super(lineNumber, columnNumber);
    }

    public abstract <R, P> R accept(AtomVisitor<R, P> visitor, P p);

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
    public final TypeDeclaration getEnclosingType()
    {
        Scope scope = this.enclosingScope;

        while (scope != null && !(scope instanceof TypeDeclaration))
        {
            scope = scope.getEnclosingScope();
        }
        if (scope instanceof TypeDeclaration)
        {
            return (TypeDeclaration) scope;
        } else {
            return null;
        }
    }

    @Override
    public final ExecutableDeclaration getEnclosingExecutable()
    {
        Scope scope = this.enclosingScope;

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
