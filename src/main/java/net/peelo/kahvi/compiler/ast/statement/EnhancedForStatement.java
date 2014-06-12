package net.peelo.kahvi.compiler.ast.statement;

import net.peelo.kahvi.compiler.ast.expression.Expression;
import net.peelo.kahvi.compiler.util.SourcePosition;

public final class EnhancedForStatement extends ContinuableStatement
{
    private final VariableStatement variable;
    private final Expression expression;
    private final Statement statement;

    public EnhancedForStatement(SourcePosition position,
                                VariableStatement variable,
                                Expression expression,
                                Statement statement)
    {
        super(position);
        (this.variable = variable).setEnclosingScope(this);
        (this.expression = expression).setEnclosingScope(this);
        (this.statement = statement).setEnclosingScope(this);
    }

    public VariableStatement getVariable()
    {
        return this.variable;
    }

    public Expression getExpression()
    {
        return this.expression;
    }

    public Statement getStatement()
    {
        return this.statement;
    }

    @Override
    public <R, P> R accept(StatementVisitor<R, P> visitor, P p)
    {
        return visitor.visitEnhancedForStatement(this, p);
    }

    @Override
    public String toString()
    {
        return "for (" + this.variable + " : " + this.expression + ")";
    }
}
