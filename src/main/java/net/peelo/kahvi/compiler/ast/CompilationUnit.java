package net.peelo.kahvi.compiler.ast;

import net.peelo.kahvi.compiler.ast.declaration.ExecutableDeclaration;
import net.peelo.kahvi.compiler.ast.declaration.ImportDeclaration;
import net.peelo.kahvi.compiler.ast.declaration.PackageDeclaration;
import net.peelo.kahvi.compiler.ast.declaration.TypeDeclaration;

import java.util.List;

public final class CompilationUnit implements Scope
{
    private final PackageDeclaration packageDeclaration;
    private final List<ImportDeclaration> imports;
    private final List<TypeDeclaration> typeDeclarations;

    public CompilationUnit(PackageDeclaration packageDeclaration,
                           List<ImportDeclaration> imports,
                           List<TypeDeclaration> typeDeclarations)
    {
        this.packageDeclaration = packageDeclaration;
        this.imports = imports;
        this.typeDeclarations = typeDeclarations;
    }

    public PackageDeclaration getPackageDeclaration()
    {
        return this.packageDeclaration;
    }

    public List<ImportDeclaration> getImports()
    {
        return this.imports;
    }

    public List<TypeDeclaration> getTypeDeclarations()
    {
        return this.typeDeclarations;
    }

    @Override
    public Scope getEnclosingScope()
    {
        return null;
    }

    @Override
    public TypeDeclaration getEnclosingType()
    {
        return null;
    }

    @Override
    public ExecutableDeclaration getEnclosingExecutable()
    {
        return null;
    }

    @Override
    public String toString()
    {
        return "<compilation unit>";
    }
}
