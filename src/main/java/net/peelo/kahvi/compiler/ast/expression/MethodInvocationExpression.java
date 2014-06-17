package net.peelo.kahvi.compiler.ast.expression;

import net.peelo.kahvi.compiler.ast.AtomVisitor;
import net.peelo.kahvi.compiler.ast.annotation.ElementValueVisitor;
import net.peelo.kahvi.compiler.ast.type.TypeArgument;
import net.peelo.kahvi.compiler.util.SourcePosition;

import java.util.Collections;
import java.util.List;

public final class MethodInvocationExpression extends Expression
{
    private final Expression methodSelect;
    private final List<TypeArgument> typeArguments;
    private final String methodName;
    private final List<Expression> arguments;

    public MethodInvocationExpression(SourcePosition position,
                                      String methodName,
                                      List<Expression> arguments)
    {
        this(position,
             null,
             Collections.<TypeArgument>emptyList(),
             methodName,
             arguments);
    }

    public MethodInvocationExpression(SourcePosition position,
                                      List<TypeArgument> typeArguments,
                                      String methodName,
                                      List<Expression> arguments)
    {
        this(position, null, typeArguments, methodName, arguments);
    }

    public MethodInvocationExpression(SourcePosition position,
                                      Expression methodSelect,
                                      String methodName,
                                      List<Expression> arguments)
    {
        this(position,
             methodSelect,
             Collections.<TypeArgument>emptyList(),
             methodName,
             arguments);
    }

    public MethodInvocationExpression(SourcePosition position,
                                      Expression methodSelect,
                                      List<TypeArgument> typeArguments,
                                      String methodName,
                                      List<Expression> arguments)
    {
        super(position);
        if ((this.methodSelect = methodSelect) != null)
        {
            this.methodSelect.setEnclosingScope(this);
        }
        for (TypeArgument ta : (this.typeArguments = typeArguments))
        {
            ta.setEnclosingScope(this);
        }
        this.methodName = methodName;
        for (Expression e : (this.arguments = arguments))
        {
            e.setEnclosingScope(this);
        }
    }

    public Expression getMethodSelect()
    {
        return this.methodSelect;
    }

    public List<TypeArgument> getTypeArguments()
    {
        return this.typeArguments;
    }

    public String getMethodName()
    {
        return this.methodName;
    }

    public List<Expression> getArguments()
    {
        return this.arguments;
    }

    @Override
    public <R, P> R accept(ExpressionVisitor<R, P> visitor, P p)
    {
        return visitor.visitMethodInvocationExpression(this, p);
    }

    @Override
    public <R, P> R accept(AtomVisitor<R, P> visitor, P p)
    {
        return visitor.visitMethodInvocationExpression(this, p);
    }

    @Override
    public <R, P> R accept(VariableInitializerVisitor<R, P> visitor, P p)
    {
        return visitor.visitMethodInvocationExpression(this, p);
    }

    @Override
    public <R, P> R accept(ElementValueVisitor<R, P> visitor, P p)
    {
        return visitor.visitMethodInvocationExpression(this, p);
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();

        if (this.methodSelect != null)
        {
            sb.append(this.methodSelect).append('.');
        }
        if (!this.typeArguments.isEmpty())
        {
            sb.append('<');
            for (int i = 0; i < 3 && i < this.typeArguments.size(); ++i)
            {
                if (i > 0)
                {
                    sb.append(", ");
                }
                sb.append(this.typeArguments.get(i));
            }
            if (this.typeArguments.size() > 3)
            {
                sb.append("...");
            }
            sb.append('>');
        }
        sb.append(this.methodName).append('(');
        for (int i = 0; i < this.arguments.size() && i < 3; ++i)
        {
            if (i > 0)
            {
                sb.append(", ");
            }
            sb.append(this.arguments.get(i));
        }
        if (this.arguments.size() > 3)
        {
            sb.append("...");
        }

        return sb.append(')').toString();
    }
}
