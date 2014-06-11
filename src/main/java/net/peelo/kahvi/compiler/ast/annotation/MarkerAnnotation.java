package net.peelo.kahvi.compiler.ast.annotation;

import net.peelo.kahvi.compiler.ast.Scope;
import net.peelo.kahvi.compiler.ast.type.ReferenceType;

public final class MarkerAnnotation implements Annotation
{
    private final ReferenceType type;

    public MarkerAnnotation(ReferenceType type)
    {
        this.type = type;
    }

    @Override
    public ReferenceType getType()
    {
        return this.type;
    }

    @Override
    public void setEnclosingScope(Scope scope)
    {
        this.type.setEnclosingScope(scope);
    }

    @Override
    public <R, P> R accept(AnnotationVisitor<R, P> visitor, P p)
    {
        return visitor.visitMarkerAnnotation(this, p);
    }

    @Override
    public <R, P> R accept(ElementValueVisitor<R, P> visitor, P p)
    {
        return visitor.visitMarkerAnnotation(this, p);
    }

    @Override
    public String toString()
    {
        return "@" + this.type;
    }
}
