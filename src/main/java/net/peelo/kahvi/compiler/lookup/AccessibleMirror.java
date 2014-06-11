package net.peelo.kahvi.compiler.lookup;

public interface AccessibleMirror
{
    boolean isPublic();

    boolean isProtected();

    boolean isDefaultAccess();
}
