package net.peelo.kahvi.compiler.ast.expression;

import net.peelo.kahvi.compiler.ast.AtomVisitor;
import net.peelo.kahvi.compiler.ast.annotation.ElementValueVisitor;
import net.peelo.kahvi.compiler.ast.type.Type;
import net.peelo.kahvi.compiler.util.SourcePosition;

public final class ClassLiteralExpression extends Expression
{
    private final Type type;

    public ClassLiteralExpression(SourcePosition position, Type type)
    {
        super(position);
        (this.type = type).setEnclosingScope(this);
    }

    public Type getType()
    {
        return this.type;
    }

    @Override
    public <R, P> R accept(ExpressionVisitor<R, P> visitor, P p)
    {
        return visitor.visitClassLiteralExpression(this, p);
    }

    @Override
    public <R, P> R accept(AtomVisitor<R, P> visitor, P p)
    {
        return visitor.visitClassLiteralExpression(this, p);
    }

    @Override
    public <R, P> R accept(ElementValueVisitor<R, P> visitor, P p)
    {
        return visitor.visitClassLiteralExpression(this, p);
    }

    @Override
    public <R, P> R accept(VariableInitializerVisitor<R, P> visitor, P p)
    {
        return visitor.visitClassLiteralExpression(this, p);
    }

    @Override
    public String toString()
    {
        return this.type + ".class";
    }
}
