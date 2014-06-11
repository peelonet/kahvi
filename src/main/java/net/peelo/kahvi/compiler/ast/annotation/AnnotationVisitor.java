package net.peelo.kahvi.compiler.ast.annotation;

public interface AnnotationVisitor<R, P>
{
    R visitMarkerAnnotation(MarkerAnnotation a, P p);
    R visitNormalAnnotation(NormalAnnotation a, P p);
    R visitSingleElementAnnotation(SingleElementAnnotation a, P p);
}
