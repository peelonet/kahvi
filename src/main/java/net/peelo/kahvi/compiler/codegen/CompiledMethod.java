package net.peelo.kahvi.compiler.codegen;

import net.peelo.kahvi.compiler.lookup.ClassMirror;
import net.peelo.kahvi.compiler.lookup.MethodMirror;
import net.peelo.kahvi.compiler.lookup.ParameterMirror;
import net.peelo.kahvi.compiler.lookup.TypeMirror;
import net.peelo.kahvi.compiler.lookup.TypeParameterMirror;

import java.util.List;

public final class CompiledMethod implements MethodMirror
{
    private final CompiledClass declaringClass;

    CompiledMethod(CompiledClass declaringClass)
    {
        this.declaringClass = declaringClass;
    }

    @Override
    public ClassMirror getDeclaringClass()
    {
        return this.declaringClass;
    }

    @Override
    public boolean isDefault()
    {
        return false; // TODO
    }

    @Override
    public List<TypeParameterMirror> getTypeParameters()
    {
        return null; // TODO
    }

    @Override
    public TypeMirror getReturnType()
    {
        return null; // TODO
    }

    @Override
    public List<ParameterMirror> getParameters()
    {
        return null; // TODO
    }

    @Override
    public boolean isVariadic()
    {
        return false; // TODO
    }

    @Override
    public boolean isFinal()
    {
        return false; // TODO
    }

    @Override
    public boolean isAbstract()
    {
        return false; // TODO
    }

    @Override
    public boolean isConstructor()
    {
        return false; // TODO
    }

    @Override
    public boolean isStatic()
    {
        return false; // TODO
    }

    @Override
    public String getName()
    {
        return null; // TODO
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
}
