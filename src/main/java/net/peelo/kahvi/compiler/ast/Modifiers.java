package net.peelo.kahvi.compiler.ast;

import net.peelo.kahvi.compiler.ast.annotation.Annotation;

import java.util.List;
import java.util.Set;

public final class Modifiers
{
    private final List<Annotation> annotations;
    private final Visibility visibility;
    private final Set<Flag> flags;

    public Modifiers(List<Annotation> annotations,
                     Visibility visibility,
                     Set<Flag> flags)
    {
        this.annotations = annotations;
        this.visibility = visibility;
        this.flags = flags;
    }

    public List<Annotation> getAnnotations()
    {
        return this.annotations;
    }

    public Visibility getVisibility()
    {
        return this.visibility;
    }

    public Set<Flag> getFlags()
    {
        return this.flags;
    }

    public boolean isAbstract()
    {
        return this.flags.contains(Flag.ABSTRACT);
    }

    public boolean isFinal()
    {
        return this.flags.contains(Flag.FINAL);
    }

    public boolean isNative()
    {
        return this.flags.contains(Flag.NATIVE);
    }

    public boolean isStatic()
    {
        return this.flags.contains(Flag.STATIC);
    }

    public boolean isStrictfp()
    {
        return this.flags.contains(Flag.STRICTFP);
    }

    public boolean isSynchronized()
    {
        return this.flags.contains(Flag.SYNCHRONIZED);
    }

    public boolean isTransient()
    {
        return this.flags.contains(Flag.TRANSIENT);
    }

    public boolean isVolatile()
    {
        return this.flags.contains(Flag.VOLATILE);
    }

    public void setEnclosingScope(Scope enclosingScope)
    {
        for (Annotation a : this.annotations)
        {
            a.setEnclosingScope(enclosingScope);
        }
    }
}
