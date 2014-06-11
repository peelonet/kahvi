package net.peelo.kahvi.compiler.ast.declaration;

import net.peelo.kahvi.compiler.ast.Node;
import net.peelo.kahvi.compiler.util.Name;

/**
 * Representation of package declaration.
 *
 * <p> For example:
 * <pre>
 * </pre>
 */
public final class PackageDeclaration extends Node
{
    /** Name of the package. */
    private final Name packageName;

    public PackageDeclaration(Name packageName,
                              int lineNumber,
                              int columnNumber)
    {
        super(lineNumber, columnNumber);
        this.packageName = packageName;
    }

    /**
     * Returns name of the package.
     */
    public Name getPackageName()
    {
        return this.packageName;
    }

    @Override
    public String toString()
    {
        return "package " + this.packageName + ";";
    }
}
