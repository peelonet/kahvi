package net.peelo.kahvi.compiler.util;

import java.io.Serializable;

/**
 * Represents position of an object in the source code.
 */
public final class SourcePosition implements Serializable
{
    private static final long serialVersionUID = 1L;

    private final int lineNumber;
    private final int columnNumber;

    public SourcePosition(int lineNumber, int columnNumber)
    {
        this.lineNumber = lineNumber;
        this.columnNumber = columnNumber;
    }

    /**
     * Returns line in the source code. The first line is {@code 1}, which
     * means that this method returns {@code 0} when no line number is
     * available.
     */
    public int getLineNumber()
    {
        return this.lineNumber;
    }

    /**
     * Returns column in the source code. The first column is {@code 1}, which
     * means that this method returns {@code 0} when no column number is
     * available.
     */
    public int getColumnNumber()
    {
        return this.columnNumber;
    }
}
