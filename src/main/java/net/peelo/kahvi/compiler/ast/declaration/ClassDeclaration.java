package net.peelo.kahvi.compiler.ast.declaration;

import net.peelo.kahvi.compiler.ast.Modifiers;
import net.peelo.kahvi.compiler.ast.type.ReferenceType;
import net.peelo.kahvi.compiler.util.SourcePosition;

import java.util.List;

public final class ClassDeclaration extends TypeDeclaration
{
    private final List<TypeParameterDeclaration> typeParameters;
    private final ReferenceType extendsClause;
    private final List<ReferenceType> implementsClause;

    public ClassDeclaration(SourcePosition position,
                            Modifiers modifiers,
                            String simpleName,
                            List<TypeParameterDeclaration> typeParameters,
                            ReferenceType extendsClause,
                            List<ReferenceType> implementsClause,
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
        for (ReferenceType rt : (this.implementsClause = implementsClause))
        {
            rt.setEnclosingScope(this);
        }
    }

    public List<TypeParameterDeclaration> getTypeParameters()
    {
        return this.typeParameters;
    }

    public ReferenceType getExtendsClause()
    {
        return this.extendsClause;
    }

    public List<ReferenceType> getImplementsClause()
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
