package net.peelo.kahvi.compiler.ast.declaration;

public interface TypeBodyDeclarationVisitor<R, P>
    extends TypeDeclarationVisitor<R, P>
{
    R visitFieldDeclaration(FieldDeclaration d, P p);
    R visitMethodDeclaration(MethodDeclaration d, P p);
    R visitReadMethodDeclaration(ReadMethodDeclaration d, P p);
    R visitWriteMethodDeclaration(WriteMethodDeclaration d, P p);
}
