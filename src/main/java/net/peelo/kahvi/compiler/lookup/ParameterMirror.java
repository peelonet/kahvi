package net.peelo.kahvi.compiler.lookup;

public interface ParameterMirror
{
    TypeMirror getType();

    boolean isNullable();
}
