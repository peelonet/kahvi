package net.peelo.kahvi.compiler.lookup;

import net.peelo.kahvi.compiler.util.Name;

import java.util.List;

public interface TypeMirror
{
    Name getQualifiedName();

    List<TypeMirror> getTypeArguments();

    TypeMirror getComponentType();

    boolean isPrimitive();

    boolean isRaw();

    TypeMirror getUpperBound();

    TypeMirror getLowerBound();

    ClassMirror getDeclaredClass();

    TypeParameterMirror getTypeParameter();
}
