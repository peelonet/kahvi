package net.peelo.kahvi.compiler.parser;

import net.peelo.kahvi.compiler.util.SourceLocatable;
import net.peelo.kahvi.compiler.util.SourcePosition;

public final class ParserException extends RuntimeException
    implements SourceLocatable
{
    private final SourcePosition position;

    ParserException(SourcePosition position, String message)
    {
        super(message);
        this.position = position;
    }

    @Override
    public SourcePosition getSourcePosition()
    {
        return this.position;
    }

    @Override
    public String getMessage()
    {
        if (this.position == null)
        {
            return super.getMessage();
        } else {
            return this.position + ": " + super.getMessage();
        }
    }
}
