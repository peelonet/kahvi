package net.peelo.kahvi.compiler.lookup;

import net.peelo.kahvi.compiler.util.Name;

import java.util.List;

public interface ClassMirror extends AnnotatedMirror, AccessibleMirror
{
    boolean isInterface();

    boolean isAnnotationType();

    boolean isEnum();

    boolean isAbstract();

    boolean isStatic();

    boolean isFinal();

    Name getQualifiedName();

    String getSimpleName();

    PackageMirror getPackage();

    List<MethodMirror> getMethods();

    List<FieldMirror> getFields();

    List<TypeParameterMirror> getTypeParameters();

    List<ClassMirror> getDeclaredClasses();

    TypeMirror getSuperclass();

    ClassMirror getEnclosingClass();

    MethodMirror getEnclosingMethod();

    List<TypeMirror> getInterfaces();
}
