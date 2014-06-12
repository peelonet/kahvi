package net.peelo.kahvi.compiler.ast.expression;

import net.peelo.kahvi.compiler.ast.AtomVisitor;
import net.peelo.kahvi.compiler.ast.annotation.ElementValueVisitor;
import net.peelo.kahvi.compiler.util.SourcePosition;

public final class AssignmentExpression extends Expression
{
    private final AssignableExpression variable;
    private final Expression expression;

    public AssignmentExpression(SourcePosition position,
                                AssignableExpression variable,
                                Expression expression)
    {
        super(position);
        (this.variable = variable).setEnclosingScope(this);
        (this.expression = expression).setEnclosingScope(this);
    }

    public AssignableExpression getVariable()
    {
        return this.variable;
    }

    public Expression getExpression()
    {
        return this.expression;
    }

    @Override
    public <R, P> R accept(ExpressionVisitor<R, P> visitor, P p)
    {
        return visitor.visitAssignmentExpression(this, p);
    }

    @Override
    public <R, P> R accept(VariableInitializerVisitor<R, P> visitor, P p)
    {
        return visitor.visitAssignmentExpression(this, p);
    }

    @Override
    public <R, P> R accept(AtomVisitor<R, P> visitor, P p)
    {
        return visitor.visitAssignmentExpression(this, p);
    }

    @Override
    public <R, P> R accept(ElementValueVisitor<R, P> visitor, P p)
    {
        return visitor.visitAssignmentExpression(this, p);
    }

    @Override
    public String toString()
    {
        return this.variable + " = " + this.expression;
    }
}
