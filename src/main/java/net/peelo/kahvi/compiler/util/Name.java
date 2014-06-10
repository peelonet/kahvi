package net.peelo.kahvi.compiler.util;

import net.peelo.kahvi.compiler.util.iterator.SingleItemIterator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Implementation of qualified identifier.
 */
public final class Name implements Serializable, Comparable<Name>, CharSequence,
       Iterable<String>
{
    private static final long serialVersionUID = 1L;

    private final Name qualifier;
    private final String simpleName;
    private transient String cachedFullName;

    /**
     * Constructs simple name without qualifier.
     *
     * @throws IllegalArgumentException If given string is not valid Java
     *                                  identifier
     */
    public Name(String simpleName)
        throws IllegalArgumentException
    {
        this(null, simpleName);
    }

    /**
     * Constructs name with an optional qualifier.
     *
     * @throws IllegalArgumentException If given string is not valid Java
     *                                  identifier
     */
    public Name(Name qualifier, String simpleName)
        throws IllegalArgumentException
    {
        if (!StringUtils.isJavaIdentifier(simpleName))
        {
            throw new IllegalArgumentException("Name is not valid identifier");
        }
        this.qualifier = qualifier;
        this.simpleName = simpleName;
    }

    /**
     * Convenience method for creating qualified names.
     *
     * <p> For example:
     * <pre>
     *   Name.of("java", "lang", "String")
     * </pre>
     */
    public static Name of(String first, String... rest)
    {
        Name name = new Name(null, first);

        for (String part : rest)
        {
            name = new Name(name, part);
        }

        return name;
    }

    /**
     * Returns the qualifier part of the name, or {@code null} if this name has
     * no qualifier.
     */
    public Name getQualifier()
    {
        return this.qualifier;
    }

    public String getSimpleName()
    {
        return this.simpleName;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        else if (o instanceof Name)
        {
            Name that = (Name) o;

            return (this.qualifier == null
                    ? that.qualifier == null
                    : this.qualifier.equals(that.qualifier))
                && this.simpleName.equals(that.simpleName);
        }
        else if (o instanceof String)
        {
            return this.toString().equals(o);
        } else {
            return false;
        }
    }

    @Override
    public int compareTo(Name that)
    {
        return this.toString().compareTo(that.toString());
    }

    @Override
    public int length()
    {
        return this.toString().length();
    }

    @Override
    public char charAt(int offset)
    {
        return this.toString().charAt(offset);
    }

    @Override
    public CharSequence subSequence(int offset, int length)
    {
        return this.toString().subSequence(offset, length);
    }

    @Override
    public Iterator<String> iterator()
    {
        if (this.qualifier != null)
        {
            List<String> list = new ArrayList<String>(3);
            Name qualifier = this.qualifier;

            list.add(this.simpleName);
            do
            {
                list.add(0, qualifier.simpleName);
                qualifier = qualifier.qualifier;
            }
            while (qualifier != null);

            return list.iterator();
        }

        return new SingleItemIterator<String>(this.simpleName);
    }

    @Override
    public synchronized String toString()
    {
        if (this.cachedFullName == null)
        {
            if (this.qualifier == null)
            {
                this.cachedFullName = this.simpleName;
            } else {
                this.cachedFullName = this.qualifier.toString()
                                                    .concat(".")
                                                    .concat(this.simpleName);
            }
        }

        return this.cachedFullName;
    }
}
