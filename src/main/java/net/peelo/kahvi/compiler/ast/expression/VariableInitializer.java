package net.peelo.kahvi.compiler.ast.expression;

import net.peelo.kahvi.compiler.ast.Scope;

public interface VariableInitializer
{
    <R, P> R accept(VariableInitializerVisitor<R, P> visitor, P p);

    void setEnclosingScope(Scope enclosingScope);
}
