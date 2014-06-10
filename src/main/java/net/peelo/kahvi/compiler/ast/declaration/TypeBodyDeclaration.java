package net.peelo.kahvi.compiler.ast.declaration;

import net.peelo.kahvi.compiler.ast.Scope;

public interface TypeBodyDeclaration extends Scope
{
    TypeDeclaration getDeclaringType();

    void setDeclaringType(TypeDeclaration declaringType);

    boolean isStatic();

    <R, P> R accept(TypeBodyDeclarationVisitor<R, P> visitor, P p);
}
