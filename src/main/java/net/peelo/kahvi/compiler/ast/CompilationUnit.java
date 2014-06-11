package net.peelo.kahvi.compiler.ast;

import net.peelo.kahvi.compiler.ast.declaration.PackageDeclaration;

public final class CompilationUnit implements Scope
{
    private final PackageDeclaration packageDeclaration;

    public CompilationUnit(PackageDeclaration packageDeclaration)
    {
        this.packageDeclaration = packageDeclaration;
    }

    public PackageDeclaration getPackageDeclaration()
    {
        return this.packageDeclaration;
    }

    @Override
    public Scope getEnclosingScope()
    {
        return null;
    }
}
