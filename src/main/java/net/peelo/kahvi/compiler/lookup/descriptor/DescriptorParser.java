package net.peelo.kahvi.compiler.lookup.descriptor;

import net.peelo.kahvi.compiler.lookup.Primitive;
import net.peelo.kahvi.compiler.util.Name;

import java.util.ArrayList;
import java.util.List;

final class DescriptorParser
{
    /** End of input marker. */
    private static final char EOI = ':';

    private final char[] input;
    private int index;

    DescriptorParser(char[] input)
    {
        this.input = input;
        this.index = 0;
    }

    private char current()
    {
        try
        {
            return this.input[this.index];
        }
        catch (ArrayIndexOutOfBoundsException ex)
        {
            return DescriptorParser.EOI;
        }
    }

    private void advance()
    {
        this.index++;
    }

    private DescriptorSyntaxException error(String message)
    {
        return new DescriptorSyntaxException(message);
    }

    MethodDescriptor parseMethodDescriptor()
        throws DescriptorSyntaxException
    {
        return new MethodDescriptor(
                this.parseFormalParameters(),
                this.parseReturnType()
        );
    }

    private TypeDescriptor parseReturnType()
        throws DescriptorSyntaxException
    {
        if (this.current() == 'V')
        {
            this.advance();

            return new VoidTypeDescriptor();
        } else {
            return this.parseTypeDescriptor();
        }
    }

    TypeDescriptor parseTypeDescriptor()
        throws DescriptorSyntaxException
    {
        switch (this.current())
        {
            case 'B':
            case 'C':
            case 'D':
            case 'F':
            case 'I':
            case 'J':
            case 'S':
            case 'Z':
                return this.parsePrimitiveTypeDescriptor();

            default:
                return this.parseReferenceTypeDescriptor();
        }
    }

    private PrimitiveTypeDescriptor parsePrimitiveTypeDescriptor()
        throws DescriptorSyntaxException
    {
        Primitive kind;

        switch (this.current())
        {
            case 'B':
                this.advance();
                kind = Primitive.BYTE;
                break;

            case 'C':
                this.advance();
                kind = Primitive.CHAR;
                break;

            case 'D':
                this.advance();
                kind = Primitive.DOUBLE;
                break;

            case 'F':
                this.advance();
                kind = Primitive.FLOAT;
                break;

            case 'I':
                this.advance();
                kind = Primitive.INT;
                break;

            case 'J':
                this.advance();
                kind = Primitive.LONG;
                break;

            case 'S':
                this.advance();
                kind = Primitive.SHORT;
                break;

            case 'Z':
                this.advance();
                kind = Primitive.BOOLEAN;
                break;

            default:
                throw this.error("expected primitive type descriptor");
        }

        return new PrimitiveTypeDescriptor(kind);
    }

    private ReferenceTypeDescriptor parseReferenceTypeDescriptor()
        throws DescriptorSyntaxException
    {
        switch (this.current())
        {
            case 'L':
                return this.parseClassTypeDescriptor();

            case '[':
                return this.parseArrayTypeDescriptor();

            default:
                throw this.error("expected reference type descriptor");
        }
    }

    private ClassTypeDescriptor parseClassTypeDescriptor()
        throws DescriptorSyntaxException
    {
        Name name = null;
        StringBuilder sb = new StringBuilder();

        if (this.current() != 'L')
        {
            throw this.error("expected class type descriptor");
        }
        this.advance();
        for (;;)
        {
            char c = this.current();

            this.advance();
            if (c == ';')
            {
                break;
            }
            else if (c == '/')
            {
                name = new Name(name, sb.toString());
                sb.setLength(0);
            }
            else if (Character.isJavaIdentifierPart(c))
            {
                sb.append(c);
            } else {
                throw this.error("expected class type descriptor");
            }
        }
        if (sb.length() > 0)
        {
            name = new Name(name, sb.toString());
        }
        if (name == null)
        {
            throw this.error("expected class type descriptor");
        }
        
        return new ClassTypeDescriptor(name);
    }

    private ArrayTypeDescriptor parseArrayTypeDescriptor()
        throws DescriptorSyntaxException
    {
        if (this.current() != '[')
        {
            throw this.error("expected array type descriptor");
        }
        this.advance();

        return this.parseTypeDescriptor().getArrayType();
    }

    private List<TypeDescriptor> parseFormalParameters()
        throws DescriptorSyntaxException
    {
        List<TypeDescriptor> tds;

        if (this.current() != '(')
        {
            throw this.error("expected (");
        }
        this.advance();
        tds = this.parseZeroOrMoreTypeDescriptors();
        if (this.current() != ')')
        {
            throw this.error("expected )");
        }
        this.advance();

        return tds;
    }

    private List<TypeDescriptor> parseZeroOrMoreTypeDescriptors()
        throws DescriptorSyntaxException
    {
        List<TypeDescriptor> list = new ArrayList<TypeDescriptor>(3);
        boolean stop = false;

        while (!stop)
        {
            switch (this.current())
            {
                case 'B':
                case 'C':
                case 'D':
                case 'F':
                case 'I':
                case 'J':
                case 'S':
                case 'Z':
                case 'L':
                case '[':
                    list.add(this.parseTypeDescriptor());
                    break;

                default:
                    stop = true;
            }
        }

        return list;
    }
}
