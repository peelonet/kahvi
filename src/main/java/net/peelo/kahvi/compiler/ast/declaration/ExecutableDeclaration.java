package net.peelo.kahvi.compiler.ast.declaration;

import net.peelo.kahvi.compiler.ast.Modifiers;
import net.peelo.kahvi.compiler.ast.expression.Expression;
import net.peelo.kahvi.compiler.ast.statement.BlockStatement;
import net.peelo.kahvi.compiler.ast.type.ClassType;
import net.peelo.kahvi.compiler.util.SourcePosition;

import java.util.List;

public abstract class ExecutableDeclaration extends AbstractTypeBodyDeclaration
{
    private final List<ParameterDeclaration> parameters;
    private final List<ClassType> _throws;
    private final List<Expression> preConditions;
    private final BlockStatement body;

    public ExecutableDeclaration(SourcePosition position,
                                 Modifiers modifiers,
                                 List<ParameterDeclaration> parameters,
                                 List<ClassType> _throws,
                                 List<Expression> preConditions,
                                 BlockStatement body)
    {
        super(position, modifiers);
        for (ParameterDeclaration pd : (this.parameters = parameters))
        {
            pd.setEnclosingScope(this);
        }
        for (ClassType ct : (this._throws = _throws))
        {
            ct.setEnclosingScope(this);
        }
        for (Expression e : (this.preConditions = preConditions))
        {
            e.setEnclosingScope(this);
        }
        if ((this.body = body) != null)
        {
            this.body.setEnclosingScope(this);
        }
    }

    public final List<ParameterDeclaration> getParameters()
    {
        return this.parameters;
    }

    public final List<ClassType> getThrows()
    {
        return this._throws;
    }

    public final List<Expression> getPreConditions()
    {
        return this.preConditions;
    }

    public final BlockStatement getBody()
    {
        return this.body;
    }
}
