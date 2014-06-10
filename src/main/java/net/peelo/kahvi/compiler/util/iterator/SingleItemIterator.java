package net.peelo.kahvi.compiler.util.iterator;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * An {@link Iterator} implementation which iterates single element.
 */
public final class SingleItemIterator<T> implements Iterator<T>
{
    private final T element;
    private boolean iterated;

    public SingleItemIterator(T element)
    {
        this.element = element;
    }

    @Override
    public boolean hasNext()
    {
        return !this.iterated;
    }

    @Override
    public T next()
    {
        if (this.iterated)
        {
            throw new NoSuchElementException();
        }
        this.iterated = true;

        return this.element;
    }

    @Override
    public void remove()
    {
        if (this.iterated)
        {
            throw new NoSuchElementException();
        }
        this.iterated = true;
    }
}
