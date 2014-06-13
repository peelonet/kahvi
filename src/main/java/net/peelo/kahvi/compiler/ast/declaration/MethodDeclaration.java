package net.peelo.kahvi.compiler.ast.declaration;

import net.peelo.kahvi.compiler.ast.Modifiers;
import net.peelo.kahvi.compiler.ast.expression.Expression;
import net.peelo.kahvi.compiler.ast.statement.BlockStatement;
import net.peelo.kahvi.compiler.ast.type.ClassType;
import net.peelo.kahvi.compiler.ast.type.Type;
import net.peelo.kahvi.compiler.util.SourcePosition;

import java.util.List;

public final class MethodDeclaration extends ExecutableDeclaration
{
    /** Optional type parameters. */
    private final List<TypeParameterDeclaration> typeParameters;
    /** Optional return type of the method. */
    private final Type returnType;
    /** Name of the method. */
    private final String name;

    public MethodDeclaration(SourcePosition position,
                             Modifiers modifiers,
                             List<TypeParameterDeclaration> typeParameters,
                             Type returnType,
                             String name,
                             List<ParameterDeclaration> parameters,
                             List<ClassType> _throws,
                             List<Expression> preConditions,
                             BlockStatement body)
    {
        super(position, modifiers, parameters, _throws, preConditions, body);
        for (TypeParameterDeclaration tpd : (this.typeParameters = typeParameters))
        {
            tpd.setEnclosingScope(this);
        }
        if ((this.returnType = returnType) != null)
        {
            this.returnType.setEnclosingScope(this);
        }
        this.name = name;
    }

    public List<TypeParameterDeclaration> getTypeParameters()
    {
        return this.typeParameters;
    }

    public Type getReturnType()
    {
        return this.returnType;
    }

    public String getName()
    {
        return this.name;
    }

    @Override
    public <R, P> R accept(TypeBodyDeclarationVisitor<R, P> visitor, P p)
    {
        return visitor.visitMethodDeclaration(this, p);
    }
}
