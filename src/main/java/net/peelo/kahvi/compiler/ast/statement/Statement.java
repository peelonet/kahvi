package net.peelo.kahvi.compiler.ast.statement;

import net.peelo.kahvi.compiler.ast.Node;
import net.peelo.kahvi.compiler.ast.Scope;

public abstract class Statement extends Node implements Scope
{
    private Scope enclosingScope;

    public Statement(int lineNumber, int columnNumber)
    {
        super(lineNumber, columnNumber);
    }

    public abstract <R, P> R accept(StatementVisitor<R, P> visitor, P p);

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
