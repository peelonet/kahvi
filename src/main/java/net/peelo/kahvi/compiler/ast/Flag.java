package net.peelo.kahvi.compiler.ast;

import java.util.Locale;

/**
 * Representation of access modifier. Visibility access modifiers are separated
 * into own enum type.
 *
 * @see net.peelo.kahvi.compiler.ast.Visibility
 */
public enum Flag
{
    ABSTRACT,
    FINAL,
    NATIVE,
    STATIC,
    STRICTFP,
    SYNCHRONIZED,
    TRANSIENT,
    VOLATILE;

    private transient String lowerCase;

    @Override
    public synchronized String toString()
    {
        if (this.lowerCase == null)
        {
            this.lowerCase = this.name().toLowerCase(Locale.US);
        }

        return this.lowerCase;
    }
}
