package net.peelo.kahvi.compiler.codegen;

import net.peelo.kahvi.compiler.ast.declaration.TypeDeclaration;
import net.peelo.kahvi.compiler.lookup.ClassMirror;
import net.peelo.kahvi.compiler.lookup.FieldMirror;
import net.peelo.kahvi.compiler.lookup.MethodMirror;
import net.peelo.kahvi.compiler.lookup.PackageMirror;
import net.peelo.kahvi.compiler.lookup.TypeMirror;
import net.peelo.kahvi.compiler.lookup.TypeParameterMirror;
import net.peelo.kahvi.compiler.util.Name;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

public final class CompiledClass implements ClassMirror
{
    private final CompilerContext context;
    private final TypeDeclaration declaration;

    CompiledClass(CompilerContext context, TypeDeclaration declaration)
    {
        this.context = context;
        this.declaration = declaration;
    }

    @Override
    public boolean isInterface()
    {
        return false; // TODO
    }

    @Override
    public boolean isAnnotationType()
    {
        return false; // TODO
    }

    @Override
    public boolean isEnum()
    {
        return false;
    }

    @Override
    public boolean isAbstract()
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
    public Name getQualifiedName()
    {
        return null; // TODO
    }

    @Override
    public String getName()
    {
        return null; // TODO
    }

    @Override
    public PackageMirror getPackage()
    {
        return null; // TODO
    }

    @Override
    public List<MethodMirror> getMethods()
    {
        return null; // TODO
    }

    @Override
    public List<FieldMirror> getFields()
    {
        return null; // TODO
    }

    @Override
    public List<TypeParameterMirror> getTypeParameters()
    {
        return null; // TODO
    }
    
    @Override
    public List<ClassMirror> getDeclaredClasses()
    {
        return null; // TODO
    }

    @Override
    public TypeMirror getSuperclass()
    {
        return null; // TODO
    }

    @Override
    public List<TypeMirror> getInterfaces()
    {
        return null; // TODO
    }
    
    @Override
    public ClassMirror getEnclosingClass()
    {
        return null; // TODO
    }
    
    @Override
    public MethodMirror getEnclosingMethod()
    {
        return null; // TODO
    }

    public void write(DataOutputStream out)
        throws IOException
    {
        // TODO: write out class bytecode
    }
}
