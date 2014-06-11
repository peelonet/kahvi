package net.peelo.kahvi.compiler.ast.annotation;

public final class ElementValuePair
{
    private final String identifier;
    private final ElementValue elementValue;

    public ElementValuePair(String identifier, ElementValue elementValue)
    {
        this.identifier = identifier;
        this.elementValue = elementValue;
    }

    public String getIdentifier()
    {
        return this.identifier;
    }

    public ElementValue getElementValue()
    {
        return this.elementValue;
    }

    @Override
    public String toString()
    {
        return this.identifier + " = " + this.elementValue;
    }
}
