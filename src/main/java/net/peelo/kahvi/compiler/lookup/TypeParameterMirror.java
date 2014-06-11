package net.peelo.kahvi.compiler.lookup;

import java.util.List;

public interface TypeParameterMirror
{
    String getName();

    List<TypeMirror> getBounds();
}
