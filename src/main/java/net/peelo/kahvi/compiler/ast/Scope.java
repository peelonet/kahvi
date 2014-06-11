package net.peelo.kahvi.compiler.ast;

import net.peelo.kahvi.compiler.ast.declaration.ExecutableDeclaration;
import net.peelo.kahvi.compiler.ast.declaration.TypeDeclaration;

public interface Scope
{
    /**
     * Returns enclosing scope.
     */
    Scope getEnclosingScope();

    /**
     * Returns the innermost type element containing the position of this
     * scope.
     */
    TypeDeclaration getEnclosingType();

    /**
     * Returns the innermost executable element containing the position of this
     * scope.
     */
    ExecutableDeclaration getEnclosingExecutable();
}
