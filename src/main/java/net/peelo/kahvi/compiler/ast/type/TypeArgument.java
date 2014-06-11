package net.peelo.kahvi.compiler.ast.type;

import net.peelo.kahvi.compiler.ast.Scope;

public interface TypeArgument
{
    <R, P> R accept(TypeArgumentVisitor<R, P> visitor, P p);

    void setEnclosingScope(Scope enclosingScope);
}
