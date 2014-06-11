package net.peelo.kahvi.compiler.ast.statement;

import net.peelo.kahvi.compiler.ast.expression.Expression;

import java.util.List;

/**
 * Representation of a 'switch' statement.
 *
 * <p> For example:
 * <pre>
 *   switch ( <em>expression</em> ) {
 *     <em>cases</em>
 *   }
 * </pre>
 */
public final class SwitchStatement extends BreakableStatement
{
    private final Expression expression;
    private final List<CaseStatement> cases;

    public SwitchStatement(Expression expression,
                           List<CaseStatement> cases,
                           int lineNumber,
                           int columnNumber)
    {
        super(lineNumber, columnNumber);
        (this.expression = expression).setEnclosingScope(this);
        for (Statement s : (this.cases = cases))
        {
            s.setEnclosingScope(this);
        }
    }

    public Expression getExpression()
    {
        return this.expression;
    }

    public List<CaseStatement> getCases()
    {
        return this.cases;
    }

    @Override
    public <R, P> R accept(StatementVisitor<R, P> visitor, P p)
    {
        return visitor.visitSwitchStatement(this, p);
    }

    @Override
    public String toString()
    {
        return "switch (" + this.expression + ") {...}";
    }
}
