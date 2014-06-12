package net.peelo.kahvi.compiler.ast.declaration;

import net.peelo.kahvi.compiler.ast.Node;
import net.peelo.kahvi.compiler.ast.Scope;
import net.peelo.kahvi.compiler.ast.annotation.Annotation;
import net.peelo.kahvi.compiler.ast.type.Type;
import net.peelo.kahvi.compiler.util.SourcePosition;

import java.util.List;

public final class ParameterDeclaration extends Node
{
    private final List<Annotation> annotations;
    private final boolean _final;
    private final Type type;
    private final boolean variadic;
    private final boolean nullable;
    private final String name;

    public ParameterDeclaration(SourcePosition position,
                                List<Annotation> annotations,
                                boolean _final,
                                Type type,
                                boolean variadic,
                                boolean nullable,
                                String name)
    {
        super(position);
        this.annotations = annotations;
        this._final = _final;
        this.type = type;
        this.variadic = variadic;
        this.nullable = nullable;
        this.name = name;
    }

    public List<Annotation> getAnnotations()
    {
        return this.annotations;
    }

    public boolean isFinal()
    {
        return this._final;
    }

    public Type getType()
    {
        return this.type;
    }

    public boolean isVariadic()
    {
        return this.variadic;
    }

    public boolean isNullable()
    {
        return this.nullable;
    }

    public String getName()
    {
        return this.name;
    }

    public void setEnclosingScope(Scope enclosingScope)
    {
        for (Annotation a : this.annotations)
        {
            a.setEnclosingScope(enclosingScope);
        }
        this.type.setEnclosingScope(enclosingScope);
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();

        if (this._final)
        {
            sb.append("final ");
        }
        sb.append(this.type);
        if (this.variadic)
        {
            sb.append("...");
        }
        if (!this.nullable)
        {
            sb.append('!');
        }
        
        return sb.append(' ').append(this.name).toString();
    }
}
