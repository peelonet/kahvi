package net.peelo.kahvi.compiler.event;

import net.peelo.kahvi.compiler.ast.Node;
import net.peelo.kahvi.compiler.util.SourcePosition;

public final class ErrorMessage extends AbstractMessage
{
    public ErrorMessage(SourcePosition position, Node source, String message)
    {
        super(position, source, message);
    }

    @Override
    public String toString()
    {
        return "error: " + this.getMessage();
    }
}
