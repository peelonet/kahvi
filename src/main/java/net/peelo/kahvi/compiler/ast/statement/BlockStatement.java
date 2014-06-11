package net.peelo.kahvi.compiler.ast.statement;

import java.util.List;

/**
 * Representation of a statement block.
 *
 * <p> For example:
 * <pre>
 *   { }
 *
 *   { <em>statements</em> }
 * </pre>
 */
public final class BlockStatement extends Statement
{
    private final List<Statement> statements;

    public BlockStatement(List<Statement> statements,
                          int lineNumber,
                          int columnNumber)
    {
        super(lineNumber, columnNumber);
        for (Statement s : (this.statements = statements))
        {
            s.setEnclosingScope(this);
        }
    }

    public List<Statement> getStatements()
    {
        return this.statements;
    }

    @Override
    public <R, P> R accept(StatementVisitor<R, P> visitor, P p)
    {
        return visitor.visitBlockStatement(this, p);
    }

    @Override
    public String toString()
    {
        return "{...}";
    }
}
