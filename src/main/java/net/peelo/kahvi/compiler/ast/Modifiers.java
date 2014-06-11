package net.peelo.kahvi.compiler.ast;

import java.util.Set;

public final class Modifiers
{
    private final Visibility visibility;
    private final Set<Modifier> modifiers;

    public Modifiers(Visibility visibility, Set<Modifier> modifiers)
    {
        this.visibility = visibility;
        this.modifiers = modifiers;
    }

    public Visibility getVisibility()
    {
        return this.visibility;
    }

    public Set<Modifier> getModifiers()
    {
        return this.modifiers;
    }

    public boolean isAbstract()
    {
        return this.modifiers.contains(Modifier.ABSTRACT);
    }

    public boolean isFinal()
    {
        return this.modifiers.contains(Modifier.FINAL);
    }

    public boolean isNative()
    {
        return this.modifiers.contains(Modifier.NATIVE);
    }

    public boolean isStatic()
    {
        return this.modifiers.contains(Modifier.STATIC);
    }

    public boolean isStrictfp()
    {
        return this.modifiers.contains(Modifier.STRICTFP);
    }

    public boolean isSynchronized()
    {
        return this.modifiers.contains(Modifier.SYNCHRONIZED);
    }

    public boolean isTransient()
    {
        return this.modifiers.contains(Modifier.TRANSIENT);
    }

    public boolean isVolatile()
    {
        return this.modifiers.contains(Modifier.VOLATILE);
    }
}
