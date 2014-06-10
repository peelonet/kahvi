package net.peelo.kahvi.compiler.ast;

/**
 * Represents position in source code.
 */
public interface SourcePosition
{
    int getLineNumber();

    int getColumnNumber();
}
