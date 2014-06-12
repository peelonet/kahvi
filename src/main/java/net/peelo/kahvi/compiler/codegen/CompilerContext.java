package net.peelo.kahvi.compiler.codegen;

import net.peelo.kahvi.compiler.ast.CompilationUnit;

public final class CompilerContext
{
    private final CompilationUnit compilationUnit;

    public CompilerContext(CompilationUnit compilationUnit)
    {
        this.compilationUnit = compilationUnit;
    }

    public CompilationUnit getCompilationUnit()
    {
        return this.compilationUnit;
    }
}
