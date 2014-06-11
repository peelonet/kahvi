package net.peelo.kahvi.compiler.ast.annotation;

public interface ElementValueArrayInitializerVisitor<R, P>
{
    R visitElementValueArrayInitializer(ElementValueArrayInitializer e, P p);
}
