package net.peelo.kahvi.compiler.ast.type;

import net.peelo.kahvi.compiler.ast.Node;

public abstract class Type extends Node
{
    public Type(int lineNumber, int columnNumber)
    {
        super(lineNumber, columnNumber);
    }
}
