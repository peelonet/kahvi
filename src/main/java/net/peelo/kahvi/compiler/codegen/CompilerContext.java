package net.peelo.kahvi.compiler.codegen;

import net.peelo.kahvi.compiler.ast.CompilationUnit;

import java.util.ArrayList;
import java.util.List;

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

    public List<CompiledClass> compile()
        throws CompilerException
    {
        List<CompiledClass> list = new ArrayList<CompiledClass>();

        // TODO: process all classes in compilation unit

        return list;
    }
}
