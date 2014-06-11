package net.peelo.kahvi.compiler.lookup;

import java.util.List;

public interface MethodMirror extends AnnotatedMirror, AccessibleMirror
{
    boolean isStatic();

    boolean isConstructor();

    boolean isAbstract();

    boolean isFinal();

    boolean isVariadic();

    List<ParameterMirror> getParameters();

    TypeMirror getReturnType();

    List<TypeParameterMirror> getTypeParameters();

    boolean isDefault();

    ClassMirror getDeclaringClass();
}
