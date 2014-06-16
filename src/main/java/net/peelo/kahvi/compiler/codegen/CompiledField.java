package net.peelo.kahvi.compiler.codegen;

import net.peelo.kahvi.compiler.ast.declaration.FieldDeclaration;
import net.peelo.kahvi.compiler.lookup.FieldMirror;
import net.peelo.kahvi.compiler.lookup.TypeMirror;

public final class CompiledField implements FieldMirror
{
    private final CompiledClass declaringClass;
    private final FieldDeclaration declaration;
    private final CompilerContext context;

    CompiledField(CompiledClass declaringClass,
                  FieldDeclaration declaration,
                  CompilerContext context)
    {
        this.declaringClass = declaringClass;
        this.declaration = declaration;
        this.context = context;
    }

    @Override
    public TypeMirror getType()
    {
        return null; // TODO
    }

    @Override
    public String getName()
    {
        return this.declaration.getName();
    }

    @Override
    public boolean isPublic()
    {
        return false; // TODO
    }

    @Override
    public boolean isProtected()
    {
        return false; // TODO
    }

    @Override
    public boolean isDefaultAccess()
    {
        return false; // TODO
    }

    @Override
    public boolean isStatic()
    {
        return false; // TODO
    }

    @Override
    public boolean isFinal()
    {
        return false; // TODO
    }
}
