package net.peelo.kahvi.compiler.ast.declaration;

import net.peelo.kahvi.compiler.ast.Modifiers;
import net.peelo.kahvi.compiler.ast.type.ClassType;
import net.peelo.kahvi.compiler.util.SourcePosition;

import java.util.List;

public final class ClassDeclaration extends TypeDeclaration
{
    private final List<TypeParameterDeclaration> typeParameters;
    private final ClassType extendsClause;
    private final List<ClassType> implementsClause;

    public ClassDeclaration(SourcePosition position,
                            Modifiers modifiers,
                            String simpleName,
                            List<TypeParameterDeclaration> typeParameters,
                            ClassType extendsClause,
                            List<ClassType> implementsClause,
                            List<TypeBodyDeclaration> members)
    {
        super(position, modifiers, simpleName, members);
        for (TypeParameterDeclaration tpd : (this.typeParameters = typeParameters))
        {
            tpd.setEnclosingScope(this);
        }
        if ((this.extendsClause = extendsClause) != null)
        {
            this.extendsClause.setEnclosingScope(this);
        }
        for (ClassType ct : (this.implementsClause = implementsClause))
        {
            ct.setEnclosingScope(this);
        }
    }

    public List<TypeParameterDeclaration> getTypeParameters()
    {
        return this.typeParameters;
    }

    public ClassType getExtendsClause()
    {
        return this.extendsClause;
    }

    public List<ClassType> getImplementsClause()
    {
        return this.implementsClause;
    }

    @Override
    public boolean isStatic()
    {
        return this.getModifiers().isStatic();
    }

    @Override
    public <R, P> R accept(TypeDeclarationVisitor<R, P> visitor, P p)
    {
        return visitor.visitClassDeclaration(this, p);
    }

    @Override
    public <R, P> R accept(TypeBodyDeclarationVisitor<R, P> visitor, P p)
    {
        return visitor.visitClassDeclaration(this, p);
    }
}
