package net.peelo.kahvi.compiler.ast.type;

public interface TypeArgumentVisitor<R, P>
{
    R visitArrayType(ArrayType t, P p);
    R visitClassType(ClassType t, P p);
    R visitWildcard(Wildcard t, P p);
}
