package net.peelo.kahvi.compiler.ast.annotation;

import net.peelo.kahvi.compiler.ast.Scope;
import net.peelo.kahvi.compiler.ast.type.ReferenceType;

public interface Annotation extends ElementValue
{
    ReferenceType getType();

    void setEnclosingScope(Scope enclosingScope);

    <R, P> R accept(AnnotationVisitor<R, P> visitor, P p);
}
