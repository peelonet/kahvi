package net.peelo.kahvi.compiler.ast.expression;

import net.peelo.kahvi.compiler.ast.AtomVisitor;
import net.peelo.kahvi.compiler.ast.annotation.ElementValueVisitor;
import net.peelo.kahvi.compiler.util.SourcePosition;

public final class MemberSelectExpression extends AssignableExpression
{
    private final Expression expression;
    private final String identifier;

    public MemberSelectExpression(SourcePosition position,
                                  Expression expression,
                                  String identifier)
    {
        super(position);
        (this.expression = expression).setEnclosingScope(this);
        this.identifier = identifier;
    }

    public Expression getExpression()
    {
        return this.expression;
    }

    public String getIdentifier()
    {
        return this.identifier;
    }

    @Override
    public <R, P> R accept(ExpressionVisitor<R, P> visitor, P p)
    {
        return visitor.visitMemberSelectExpression(this, p);
    }

    @Override
    public <R, P> R accept(AssignableExpressionVisitor<R, P> visitor, P p)
    {
        return visitor.visitMemberSelectExpression(this, p);
    }

    @Override
    public <R, P> R accept(AtomVisitor<R, P> visitor, P p)
    {
        return visitor.visitMemberSelectExpression(this, p);
    }

    @Override
    public <R, P> R accept(VariableInitializerVisitor<R, P> visitor, P p)
    {
        return visitor.visitMemberSelectExpression(this, p);
    }

    @Override
    public <R, P> R accept(ElementValueVisitor<R, P> visitor, P p)
    {
        return visitor.visitMemberSelectExpression(this, p);
    }

    @Override
    public String toString()
    {
        return this.expression + "." + this.identifier;
    }
}
