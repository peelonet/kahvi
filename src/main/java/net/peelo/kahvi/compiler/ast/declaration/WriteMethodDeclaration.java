package net.peelo.kahvi.compiler.ast.declaration;

import net.peelo.kahvi.compiler.ast.Modifiers;
import net.peelo.kahvi.compiler.ast.expression.Expression;
import net.peelo.kahvi.compiler.ast.statement.BlockStatement;
import net.peelo.kahvi.compiler.ast.type.ClassType;
import net.peelo.kahvi.compiler.util.SourcePosition;

import java.util.List;

public final class WriteMethodDeclaration extends ExecutableDeclaration
{
    public WriteMethodDeclaration(SourcePosition position,
                                  Modifiers modifiers,
                                  List<ParameterDeclaration> parameters,
                                  List<ClassType> _throws,
                                  List<Expression> preConditions,
                                  BlockStatement body)
    {
        super(position,
              modifiers,
              parameters,
              _throws,
              preConditions,
              body);
    }

    @Override
    public <R, P> R accept(TypeBodyDeclarationVisitor<R, P> visitor, P p)
    {
        return visitor.visitWriteMethodDeclaration(this, p);
    }
}
