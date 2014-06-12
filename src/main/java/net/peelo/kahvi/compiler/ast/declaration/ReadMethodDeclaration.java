package net.peelo.kahvi.compiler.ast.declaration;

import net.peelo.kahvi.compiler.ast.Modifiers;
import net.peelo.kahvi.compiler.ast.expression.Expression;
import net.peelo.kahvi.compiler.ast.statement.BlockStatement;
import net.peelo.kahvi.compiler.ast.type.ClassType;
import net.peelo.kahvi.compiler.util.SourcePosition;

import java.util.Collections;
import java.util.List;

public final class ReadMethodDeclaration extends ExecutableDeclaration
{
    public ReadMethodDeclaration(SourcePosition position,
                                 Modifiers modifiers,
                                 List<ClassType> _throws,
                                 List<Expression> preConditions,
                                 BlockStatement body)
    {
        super(position,
              modifiers,
              Collections.<ParameterDeclaration>emptyList(),
              _throws,
              preConditions,
              body);
    }

    @Override
    public <R, P> R accept(TypeBodyDeclarationVisitor<R, P> visitor, P p)
    {
        return visitor.visitReadMethodDeclaration(this, p);
    }
}
