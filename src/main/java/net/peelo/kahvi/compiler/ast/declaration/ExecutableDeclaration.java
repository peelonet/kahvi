package net.peelo.kahvi.compiler.ast.declaration;

import net.peelo.kahvi.compiler.ast.Modifiers;
import net.peelo.kahvi.compiler.ast.statement.Statement;

import java.util.List;

public abstract class ExecutableDeclaration extends AbstractTypeBodyDeclaration
{
    private final List<Statement> statements;

    public ExecutableDeclaration(Modifiers modifiers,
                                 List<Statement> statements,
                                 int lineNumber,
                                 int columnNumber)
    {
        super(modifiers, lineNumber, columnNumber);
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
