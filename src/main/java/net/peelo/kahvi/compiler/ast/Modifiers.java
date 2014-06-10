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
}
