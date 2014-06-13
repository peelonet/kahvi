package net.peelo.kahvi.compiler.ast.statement;

import net.peelo.kahvi.compiler.ast.expression.VariableInitializer;
import net.peelo.kahvi.compiler.ast.type.Type;
import net.peelo.kahvi.compiler.util.SourcePosition;

public final class VariableStatement extends Statement
{
    private final boolean _final;
    private final Type type;
    private final boolean nullable;
    private final String name;
    private final VariableInitializer initializer;

    public VariableStatement(SourcePosition position,
                             boolean _final,
                             Type type,
                             boolean nullable,
                             String name,
                             VariableInitializer initializer)
    {
        super(position);
        this._final = _final;
        if ((this.type = type) != null)
        {
            this.type.setEnclosingScope(this);
        }
        this.nullable = nullable;
        this.name = name;
        if ((this.initializer = initializer) != null)
        {
            this.initializer.setEnclosingScope(this);
        }
    }

    public boolean isFinal()
    {
        return this._final;
    }

    public Type getType()
    {
        return this.type;
    }

    public boolean isNullable()
    {
        return this.nullable;
    }

    public String getName()
    {
        return this.name;
    }

    public VariableInitializer getInitializer()
    {
        return this.initializer;
    }

    @Override
    public <R, P> R accept(StatementVisitor<R, P> visitor, P p)
    {
        return visitor.visitVariableStatement(this, p);
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();

        if (this._final)
        {
            sb.append("final ");
        }
        if (this.type == null)
        {
            sb.append("var");
        } else {
            sb.append(this.type);
        }
        if (!this.nullable)
        {
            sb.append('!');
        }
        sb.append(' ').append(this.name);
        if (this.initializer != null)
        {
            sb.append(" = ").append(this.initializer);
        }

        return sb.append(';').toString();
    }
}
