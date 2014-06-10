package net.peelo.kahvi.compiler.ast;

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
}
