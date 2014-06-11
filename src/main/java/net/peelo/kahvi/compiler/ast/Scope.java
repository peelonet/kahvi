package net.peelo.kahvi.compiler.ast;

public interface Scope
{
    /**
     * Returns enclosing scope.
     */
    Scope getEnclosingScope();
}
