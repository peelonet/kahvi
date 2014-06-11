package net.peelo.kahvi.compiler.ast.annotation;

import net.peelo.kahvi.compiler.ast.Scope;
import net.peelo.kahvi.compiler.ast.type.ReferenceType;

public final class SingleElementAnnotation implements Annotation
{
    private final ReferenceType type;
    private final ElementValue elementValue;

    public SingleElementAnnotation(ReferenceType type,
                                   ElementValue elementValue)
    {
        this.type = type;
        this.elementValue = elementValue;
    }

    @Override
    public ReferenceType getType()
    {
        return this.type;
    }

    public ElementValue getElementValue()
    {
        return this.elementValue;
    }

    @Override
    public void setEnclosingScope(Scope scope)
    {
        this.type.setEnclosingScope(scope);
    }

    @Override
    public <R, P> R accept(AnnotationVisitor<R, P> visitor, P p)
    {
        return visitor.visitSingleElementAnnotation(this, p);
    }

    @Override
    public <R, P> R accept(ElementValueVisitor<R, P> visitor, P p)
    {
        return visitor.visitSingleElementAnnotation(this, p);
    }

    @Override
    public String toString()
    {
        return "@" + this.type + "(" + this.elementValue + ")";
    }
}
