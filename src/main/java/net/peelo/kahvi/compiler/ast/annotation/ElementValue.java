package net.peelo.kahvi.compiler.ast.annotation;

public interface ElementValue
{
    <R, P> R accept(ElementValueVisitor<R, P> visitor, P p);
}
