package net.peelo.kahvi.compiler.ast.declaration;

import net.peelo.kahvi.compiler.ast.Modifiers;
import net.peelo.kahvi.compiler.ast.Node;
import net.peelo.kahvi.compiler.ast.expression.VariableInitializer;
import net.peelo.kahvi.compiler.ast.type.Type;
import net.peelo.kahvi.compiler.util.SourcePosition;

import java.util.List;

public final class FieldDeclaration extends AbstractTypeBodyDeclaration
{
    private final Type type;
    private final String name;
    private final VariableInitializer initializer;
    private final ReadMethodDeclaration readMethod;
    private final List<WriteMethodDeclaration> writeMethods;

    public FieldDeclaration(SourcePosition position,
                            Modifiers modifiers,
                            Type type,
                            String name,
                            VariableInitializer initializer,
                            ReadMethodDeclaration readMethod,
                            List<WriteMethodDeclaration> writeMethods)
    {
        super(position, modifiers);
        (this.type = type).setEnclosingScope(this);
        this.name = name;
        if ((this.initializer = initializer) != null)
        {
            this.initializer.setEnclosingScope(this);
        }
        this.readMethod = readMethod; // TODO: set enclosing scope
        this.writeMethods = writeMethods; // TODO: set enclosing scope
    }

    public Type getType()
    {
        return this.type;
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
    public <R, P> R accept(TypeBodyDeclarationVisitor<R, P> visitor, P p)
    {
        return visitor.visitFieldDeclaration(this, p);
    }
}
