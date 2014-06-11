package net.peelo.kahvi.compiler.ast.type;

public abstract class ReferenceType extends Type
{
    public ReferenceType(int lineNumber, int columnNumber)
    {
        super(lineNumber, columnNumber);
    }
}
