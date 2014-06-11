package net.peelo.kahvi.compiler.lookup.descriptor;

/**
 * An exception type which is thrown when parsing of type descriptor or method
 * descriptor fails.
 */
public final class DescriptorSyntaxException extends Exception
{
    DescriptorSyntaxException(String message)
    {
        super(message);
    }
}
