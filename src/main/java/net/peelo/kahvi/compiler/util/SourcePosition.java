package net.peelo.kahvi.compiler.util;

import java.io.File;
import java.io.Serializable;

/**
 * Represents position of an object in the source code.
 */
public final class SourcePosition
    implements Serializable, Comparable<SourcePosition>
{
    private static final long serialVersionUID = 1L;

    private final File file;
    private final int lineNumber;
    private final int columnNumber;

    public SourcePosition(File file, int lineNumber, int columnNumber)
    {
        this.file = file;
        this.lineNumber = lineNumber;
        this.columnNumber = columnNumber;
    }

    
    /**
     * Returns the source file or {@code null} if no file information is
     * available.
     */
    public File getFile()
    {
        return this.file;
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

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        else if (obj instanceof SourcePosition)
        {
            SourcePosition that = (SourcePosition) obj;

            return (this.file == null ?
                    that.file == null :
                    this.file.equals(that.file))
                && this.lineNumber == that.lineNumber
                && this.columnNumber == that.columnNumber;
        }

        return false;
    }

    @Override
    public int compareTo(SourcePosition that)
    {
        if (this.lineNumber != that.lineNumber)
        {
            return this.lineNumber > that.lineNumber ? 1 : -1;
        }
        else if (this.columnNumber != that.columnNumber)
        {
            return this.columnNumber > that.columnNumber ? 1 : -1;
        } else {
            return 0;
        }
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();

        if (this.file != null)
        {
            sb.append(this.file).append(':');
        }
        if (this.lineNumber > 0)
        {
            sb.append(this.lineNumber);
        }

        return sb.toString();
    }
}
