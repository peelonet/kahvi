package net.peelo.kahvi.compiler.ast.annotation;

import java.util.List;

public final class ElementValueArrayInitializer implements ElementValue
{
    private final List<ElementValue> elementValues;

    public ElementValueArrayInitializer(List<ElementValue> elementValues)
    {
        this.elementValues = elementValues;
    }

    public List<ElementValue> getElementValues()
    {
        return this.elementValues;
    }

    @Override
    public <R, P> R accept(ElementValueVisitor<R, P> visitor, P p)
    {
        return visitor.visitElementValueArrayInitializer(this, p);
    }

    @Override
    public String toString()
    {
        switch (this.elementValues.size())
        {
            case 0:
                return "{}";

            case 1:
                return "{ " + this.elementValues.get(0) + " }";

            default:
                return "{ " + this.elementValues.get(0) + ", ... }";
        }
    }
}
