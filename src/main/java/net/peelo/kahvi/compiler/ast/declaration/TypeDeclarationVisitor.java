package net.peelo.kahvi.compiler.ast.declaration;

public interface TypeDeclarationVisitor<R, P>
{
    R visitClassDeclaration(ClassDeclaration t, P p);
}
