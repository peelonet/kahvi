package net.peelo.kahvi.compiler.ast.expression;

import net.peelo.kahvi.compiler.ast.AtomVisitor;
import net.peelo.kahvi.compiler.ast.annotation.ElementValueVisitor;
import net.peelo.kahvi.compiler.util.SourcePosition;

public final class BinaryExpression extends Expression
{
    private final Expression leftOperand;
    private final Kind kind;
    private final Expression rightOperand;

    public enum Kind
    {
        ADD("+"),
        SUB("-"),
        MUL("*"),
        DIV("/"),
        MOD("%"),
        AND("&&"),
        OR("||"),
        BIT_AND("&"),
        BIT_OR("|"),
        BIT_XOR("^"),
        LSH("<<"),
        RSH(">>"),
        RSH2(">>>"),
        EQ("=="),
        NE("!="),
        LT("<"),
        GT(">"),
        LTE("<="),
        GTE(">=");

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

    public BinaryExpression(SourcePosition position,
                            Expression leftOperand,
                            Kind kind,
                            Expression rightOperand)
    {
        super(position);
        (this.leftOperand = leftOperand).setEnclosingScope(this);
        this.kind = kind;
        (this.rightOperand = rightOperand).setEnclosingScope(this);
    }

    public Expression getLeftOperand()
    {
        return this.leftOperand;
    }

    public Kind getKind()
    {
        return this.kind;
    }

    public Expression getRightOperand()
    {
        return this.rightOperand;
    }

    @Override
    public <R, P> R accept(ExpressionVisitor<R, P> visitor, P p)
    {
        return visitor.visitBinaryExpression(this, p);
    }

    @Override
    public <R, P> R accept(VariableInitializerVisitor<R, P> visitor, P p)
    {
        return visitor.visitBinaryExpression(this, p);
    }

    @Override
    public <R, P> R accept(AtomVisitor<R, P> visitor, P p)
    {
        return visitor.visitBinaryExpression(this, p);
    }

    @Override
    public <R, P> R accept(ElementValueVisitor<R, P> visitor, P p)
    {
        return visitor.visitBinaryExpression(this, p);
    }

    @Override
    public String toString()
    {
        return this.leftOperand + " " + this.kind + " " + this.rightOperand;
    }
}
