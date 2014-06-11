package net.peelo.kahvi.compiler.ast.expression;

import net.peelo.kahvi.compiler.ast.AtomVisitor;
import net.peelo.kahvi.compiler.ast.annotation.ElementValueVisitor;
import net.peelo.kahvi.compiler.util.SourcePosition;

public final class CompoundAssignmentExpression extends Expression
{
    private final AssignableExpression variable;
    private final Kind kind;
    private final Expression expression;

    public enum Kind
    {
        ADD("+="),
        SUB("-="),
        MUL("*="),
        DIV("/="),
        MOD("%="),
        LSH("<<="),
        RSH(">>="),
        RSH2(">>>="),
        BIT_AND("&="),
        BIT_OR("|="),
        BIT_XOR("^=");

        private final String description;

        private Kind(String description)
        {
            this.description = description;
        }

        @Override
        public String toString()
        {
            return this.description;
        }
    }

    public CompoundAssignmentExpression(SourcePosition position,
                                        AssignableExpression variable,
                                        Kind kind,
                                        Expression expression)
    {
        super(position);
        (this.variable = variable).setEnclosingScope(this);
        this.kind = kind;
        (this.expression = expression).setEnclosingScope(this);
    }

    public AssignableExpression getVariable()
    {
        return this.variable;
    }

    public Kind getKind()
    {
        return this.kind;
    }

    public Expression getExpression()
    {
        return this.expression;
    }

    @Override
    public <R, P> R accept(ExpressionVisitor<R, P> visitor, P p)
    {
        return visitor.visitCompoundAssignmentExpression(this, p);
    }

    @Override
    public <R, P> R accept(AtomVisitor<R, P> visitor, P p)
    {
        return visitor.visitCompoundAssignmentExpression(this, p);
    }

    @Override
    public <R, P> R accept(ElementValueVisitor<R, P> visitor, P p)
    {
        return visitor.visitCompoundAssignmentExpression(this, p);
    }

    @Override
    public String toString()
    {
        return this.variable + " " + this.kind + " " + this.expression;
    }
}
