package net.peelo.kahvi.compiler.codegen;

import net.peelo.kahvi.compiler.util.SourceLocatable;
import net.peelo.kahvi.compiler.util.SourcePosition;

public final class CompilerException extends RuntimeException
    implements SourceLocatable
{
    private final SourcePosition position;

    CompilerException(SourcePosition position, String message)
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
