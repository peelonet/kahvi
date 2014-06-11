package net.peelo.kahvi.compiler.ast.statement;

import net.peelo.kahvi.compiler.ast.expression.Expression;

import java.util.List;

/**
 * Representation of a 'case' in a 'switch' statement.
 *
 * <p> For example:
 * <pre>
 *   case <em>expression</em> :
 *       <em>statements</em>
 *
 *   default :
 *       <em>statements</em>
 * </pre>
 *
 * @see net.peelo.kahvi.compiler.ast.statement.SwitchStatement
 */
public final class CaseStatement extends Statement
{
    private final Expression expression;
    private final List<Statement> statements;

    public CaseStatement(List<Statement> statements,
                         int lineNumber,
                         int columnNumber)
    {
        this(null, statements, lineNumber, columnNumber);
    }

    public CaseStatement(Expression expression,
                         List<Statement> statements,
                         int lineNumber,
                         int columnNumber)
    {
        super(lineNumber, columnNumber);
        if ((this.expression = expression) != null)
        {
            this.expression.setEnclosingScope(this);
        }
        for (Statement s : (this.statements = statements))
        {
            s.setEnclosingScope(this);
        }
    }

    public Expression getExpression()
    {
        return this.expression;
    }

    public List<Statement> getStatements()
    {
        return this.statements;
    }

    @Override
    public <R, P> R accept(StatementVisitor<R, P> visitor, P p)
    {
        return visitor.visitCaseStatement(this, p);
    }

    @Override
    public String toString()
    {
        if (this.expression == null)
        {
            return "default:";
        } else {
            return "case " + this.expression + ":";
        }
    }
}
