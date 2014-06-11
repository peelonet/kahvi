package net.peelo.kahvi.compiler.ast;

import net.peelo.kahvi.compiler.util.SourceLocatable;
import net.peelo.kahvi.compiler.util.SourcePosition;

/**
 * Common super class for all <abbr title="Abstract Syntax Tree">AST</abbr>
 * nodes.
 */
public abstract class Node implements SourceLocatable
{
    private final SourcePosition sourcePosition;

    public Node(SourcePosition sourcePosition)
    {
        this.sourcePosition = sourcePosition;
    }

    @Override
    public final SourcePosition getSourcePosition()
    {
        return this.sourcePosition;
    }
}
