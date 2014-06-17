package net.peelo.kahvi.compiler.codegen;

import net.peelo.kahvi.compiler.ast.CompilationUnit;
import net.peelo.kahvi.compiler.ast.declaration.ClassDeclaration;
import net.peelo.kahvi.compiler.ast.declaration.TypeDeclaration;
import net.peelo.kahvi.compiler.ast.declaration.TypeDeclarationVisitor;

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

        for (TypeDeclaration td : this.compilationUnit.getTypeDeclarations())
        {
            td.accept(new TypeDeclarationVisitor<Void, List<CompiledClass>>()
            {
                @Override
                public Void visitClassDeclaration(ClassDeclaration cd,
                                                  List<CompiledClass> list)
                {
                    // TODO

                    return null;
                }
            }, list);
        }

        return list;
    }
}
