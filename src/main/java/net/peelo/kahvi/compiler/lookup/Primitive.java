package net.peelo.kahvi.compiler.lookup;

import java.util.Locale;

public enum Primitive
{
    BYTE,
    CHAR,
    SHORT,
    INT,
    LONG,
    FLOAT,
    DOUBLE,
    BOOLEAN;

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
