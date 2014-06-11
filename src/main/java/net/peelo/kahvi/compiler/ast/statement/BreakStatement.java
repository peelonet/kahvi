package net.peelo.kahvi.compiler.ast.statement;

import net.peelo.kahvi.compiler.util.Name;
import net.peelo.kahvi.compiler.util.SourcePosition;

/**
 * Representation of a 'break' statement.
 *
 * <p> For example:
 * <pre>
 *   break;
 *
 *   break <em>label</em> ;
 * </pre>
 */
public final class BreakStatement extends Statement
{
    private final Name label;

    public BreakStatement(SourcePosition position)
    {
        this(position, null);
    }

    public BreakStatement(SourcePosition position, Name label)
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
        return visitor.visitBreakStatement(this, p);
    }

    @Override
    public String toString()
    {
        if (this.label == null)
        {
            return "break;";
        } else {
            return "break " + this.label + ";";
        }
    }
}
