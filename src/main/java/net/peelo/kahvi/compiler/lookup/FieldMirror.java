package net.peelo.kahvi.compiler.lookup;

public interface FieldMirror extends AnnotatedMirror, AccessibleMirror
{
    boolean isStatic();

    boolean isFinal();

    TypeMirror getType();

    String getName();
}
