package net.peelo.kahvi.compiler.ast.declaration;

import net.peelo.kahvi.compiler.ast.Modifiers;
import net.peelo.kahvi.compiler.ast.statement.BlockStatement;
import net.peelo.kahvi.compiler.util.SourcePosition;

import java.util.List;

public abstract class ExecutableDeclaration extends AbstractTypeBodyDeclaration
{
    private final BlockStatement body;

    public ExecutableDeclaration(SourcePosition position,
                                 Modifiers modifiers,
                                 BlockStatement body)
    {
        super(position, modifiers);
        if ((this.body = body) != null)
        {
            this.body.setEnclosingScope(this);
        }
    }

    public final BlockStatement getBody()
    {
        return this.body;
    }
}
