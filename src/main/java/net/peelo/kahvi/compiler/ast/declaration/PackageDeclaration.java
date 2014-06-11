package net.peelo.kahvi.compiler.ast.declaration;

import net.peelo.kahvi.compiler.ast.Node;
import net.peelo.kahvi.compiler.ast.Scope;
import net.peelo.kahvi.compiler.ast.annotation.Annotation;
import net.peelo.kahvi.compiler.util.Name;
import net.peelo.kahvi.compiler.util.SourcePosition;

import java.util.List;

/**
 * Representation of package declaration.
 *
 * <p> For example:
 * <pre>
 *   package <em>qualifiedIdentifier</em> ;
 * </pre>
 */
public final class PackageDeclaration extends Node
{
    /** Package annotations. */
    private final List<Annotation> annotations;
    /** Name of the package. */
    private final Name packageName;

    public PackageDeclaration(SourcePosition position,
                              List<Annotation> annotations,
                              Name packageName)
    {
        super(position);
        this.annotations = annotations;
        this.packageName = packageName;
    }

    /**
     * Returns list of package annotations.
     */
    public List<Annotation> getAnnotations()
    {
        return this.annotations;
    }

    /**
     * Returns name of the package.
     */
    public Name getPackageName()
    {
        return this.packageName;
    }

    public void setEnclosingScope(Scope enclosingScope)
    {
        for (Annotation a : this.annotations)
        {
            a.setEnclosingScope(enclosingScope);
        }
    }

    @Override
    public String toString()
    {
        return "package " + this.packageName + ";";
    }
}
