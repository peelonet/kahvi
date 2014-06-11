package net.peelo.kahvi.compiler.ast.annotation;

import net.peelo.kahvi.compiler.ast.Scope;
import net.peelo.kahvi.compiler.ast.type.ReferenceType;

import java.util.List;

public final class NormalAnnotation implements Annotation
{
    private final ReferenceType type;
    private final List<ElementValuePair> elementValuePairs;

    public NormalAnnotation(ReferenceType type,
                            List<ElementValuePair> elementValuePairs)
    {
        this.type = type;
        this.elementValuePairs = elementValuePairs;
    }

    @Override
    public ReferenceType getType()
    {
        return this.type;
    }

    public List<ElementValuePair> getElementValuePairs()
    {
        return this.elementValuePairs;
    }

    @Override
    public void setEnclosingScope(Scope scope)
    {
        this.type.setEnclosingScope(scope);
    }

    @Override
    public <R, P> R accept(AnnotationVisitor<R, P> visitor, P p)
    {
        return visitor.visitNormalAnnotation(this, p);
    }

    @Override
    public <R, P> R accept(ElementValueVisitor<R, P> visitor, P p)
    {
        return visitor.visitNormalAnnotation(this, p);
    }

    @Override
    public String toString()
    {
        switch (this.elementValuePairs.size())
        {
            case 0:
                return "@" + this.type + "()";

            case 1:
                return "@" + this.type + "(" + this.elementValuePairs.get(0) + ")";

            default:
                return "@" + this.type + "(" + this.elementValuePairs.get(0) + ", ...)";
        }
    }
}
