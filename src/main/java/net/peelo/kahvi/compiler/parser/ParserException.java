package net.peelo.kahvi.compiler.parser;

import net.peelo.kahvi.compiler.ast.SourcePosition;

public final class ParserException extends RuntimeException
    implements SourcePosition
{
    private final SourcePosition sourcePosition;

    ParserException(SourcePosition sourcePosition, String message)
    {
        super(message);
        this.sourcePosition = sourcePosition;
    }

    @Override
    public String getMessage()
    {
        if (this.sourcePosition != null)
        {
            StringBuilder sb = new StringBuilder();

            sb.append("Line: ")
              .append(this.sourcePosition.getLineNumber())
              .append(", Column: ").append(this.sourcePosition.getColumnNumber())
              .append(": ").append(super.getMessage());

            return sb.toString();
        }

        return super.getMessage();
    }

    @Override
    public int getLineNumber()
    {
        if (this.sourcePosition == null)
        {
            return 0;
        } else {
            return this.sourcePosition.getLineNumber();
        }
    }

    @Override
    public int getColumnNumber()
    {
        if (this.sourcePosition == null)
        {
            return 0;
        } else {
            return this.sourcePosition.getColumnNumber();
        }
    }
}
