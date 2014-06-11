package net.peelo.kahvi.compiler.util;

/**
 * Common interface for all objects which can be traced into source code.
 */
public interface SourceLocatable
{
    /**
     * Returns position of the object in the source code.
     */
    SourcePosition getSourcePosition();
}
