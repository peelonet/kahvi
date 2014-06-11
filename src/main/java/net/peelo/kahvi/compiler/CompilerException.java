package net.peelo.kahvi.compiler;

import net.peelo.kahvi.compiler.ast.SourcePosition;

public final class CompilerException extends RuntimeException
    implements SourcePosition
{
    private final int lineNumber;
    private final int columnNumber;

    CompilerException(String message, int lineNumber, int columnNumber)
    {
        super(message);
        this.lineNumber = lineNumber;
        this.columnNumber = columnNumber;
    }

    @Override
    public String getMessage()
    {
        StringBuilder sb = new StringBuilder();

        if (this.lineNumber > 0)
        {
            sb.append("Line: ").append(this.lineNumber).append(": ");
        }
        if (this.columnNumber > 0)
        {
            sb.append("Column: ").append(this.columnNumber).append(": ");
        }
        
        return sb.append(super.getMessage()).toString();
    }

    @Override
    public int getLineNumber()
    {
        return this.lineNumber;
    }

    @Override
    public int getColumnNumber()
    {
        return this.columnNumber;
    }
}
