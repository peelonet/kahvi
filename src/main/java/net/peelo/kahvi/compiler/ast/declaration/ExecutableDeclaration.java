package net.peelo.kahvi.compiler.ast.declaration;

import net.peelo.kahvi.compiler.ast.Modifiers;
import net.peelo.kahvi.compiler.ast.statement.Statement;
import net.peelo.kahvi.compiler.util.SourcePosition;

import java.util.List;

public abstract class ExecutableDeclaration extends AbstractTypeBodyDeclaration
{
    private final List<Statement> statements;

    public ExecutableDeclaration(SourcePosition position,
                                 Modifiers modifiers,
                                 List<Statement> statements)
    {
        super(position, modifiers);
        if ((this.statements = statements) != null)
        {
            for (Statement s : this.statements)
            {
                s.setEnclosingScope(this);
            }
        }
    }

    public final List<Statement> getStatements()
    {
        return this.statements;
    }
}
