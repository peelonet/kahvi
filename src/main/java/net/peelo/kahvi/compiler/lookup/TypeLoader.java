package net.peelo.kahvi.compiler.lookup;

import net.peelo.kahvi.compiler.lookup.descriptor.*;

public abstract class TypeLoader
{
    private final TypeLoader parentTypeLoader;

    public TypeLoader(TypeLoader parentTypeLoader)
    {
        this.parentTypeLoader = parentTypeLoader;
    }

    public final TypeLoader getParentTypeLoader()
    {
        return this.parentTypeLoader;
    }

    public final TypeMirror loadType(TypeDescriptor descriptor)
    {
        return descriptor.accept(new TypeDescriptorVisitor<TypeMirror, TypeLoader>()
        {
            @Override
            public TypeMirror visitArrayTypeDescriptor(ArrayTypeDescriptor descriptor,
                                                       TypeLoader loader)
            {
                return null; // TODO
            }

            @Override
            public TypeMirror visitClassTypeDescriptor(ClassTypeDescriptor descriptor,
                                                       TypeLoader loader)
            {
                return null; // TODO
            }

            @Override
            public TypeMirror visitPrimitiveTypeDescriptor(PrimitiveTypeDescriptor descriptor,
                                                           TypeLoader loader)
            {
                return null; // TODO
            }

            @Override
            public TypeMirror visitVoidTypeDescriptor(VoidTypeDescriptor descriptor,
                                                      TypeLoader loader)
            {
                return null; // TODO
            }
        }, this);
    }
}
