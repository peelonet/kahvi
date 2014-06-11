package net.peelo.kahvi.compiler.ast.type;

public interface TypeVisitor<R, P>
{
    R visitArrayType(ArrayType t, P p);
    R visitPrimitiveType(PrimitiveType t, P p);
    R visitVoidType(VoidType t, P p);
}
