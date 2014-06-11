package net.peelo.kahvi.compiler.ast.statement;

import net.peelo.kahvi.compiler.util.Name;
import net.peelo.kahvi.compiler.util.SourcePosition;

/**
 * Representation of a 'continue' statement.
 *
 * <p> For example:
 * <pre>
 *   continue;
 *
 *   continue <em>label</em> ;
 * </pre>
 */
public final class ContinueStatement extends Statement
{
    private final Name label;

    public ContinueStatement(SourcePosition position)
    {
        this(position, null);
    }

    public ContinueStatement(SourcePosition position, Name label)
    {
        super(position);
        this.label = label;
    }

    public Name getLabel()
    {
        return this.label;
    }

    @Override
    public <R, P> R accept(StatementVisitor<R, P> visitor, P p)
    {
        return visitor.visitContinueStatement(this, p);
    }

    @Override
    public String toString()
    {
        if (this.label == null)
        {
            return "continue;";
        } else {
            return "continue " + this.label + ";";
        }
    }
}
