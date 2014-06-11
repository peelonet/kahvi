package net.peelo.kahvi.compiler.ast.annotation;

import net.peelo.kahvi.compiler.ast.expression.ExpressionVisitor;

public interface ElementValueVisitor<R, P>
    extends ExpressionVisitor<R, P>,
            AnnotationVisitor<R, P>,
            ElementValueArrayInitializerVisitor<R, P> {}
