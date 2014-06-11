package net.peelo.kahvi.compiler.parser;

import net.peelo.kahvi.compiler.ast.Atom;
import net.peelo.kahvi.compiler.ast.CompilationUnit;
import net.peelo.kahvi.compiler.ast.Modifiers;
import net.peelo.kahvi.compiler.ast.annotation.*;
import net.peelo.kahvi.compiler.ast.declaration.*;
import net.peelo.kahvi.compiler.ast.expression.*;
import net.peelo.kahvi.compiler.ast.statement.*;
import net.peelo.kahvi.compiler.util.Name;
import net.peelo.kahvi.compiler.util.SourceLocatable;
import net.peelo.kahvi.compiler.util.SourcePosition;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Parser implements SourceLocatable
{
    private final Scanner scanner;

    public Parser(Scanner scanner)
    {
        this.scanner = scanner;
    }

    public Scanner getScanner()
    {
        return this.scanner;
    }

    @Override
    public SourcePosition getSourcePosition()
    {
        return this.scanner.getSourcePosition();
    }

    /**
     * <pre>
     *   CompilationUnit:
     *     [PackageDeclaration] {ImportDeclaration} {TypeDeclaration}
     * </pre>
     */
    public CompilationUnit parseCompilationUnit()
        throws ParserException, IOException
    {
        PackageDeclaration packageDeclaration = null;
        List<ImportDeclaration> imports = new ArrayList<ImportDeclaration>(3);
        List<TypeDeclaration> typeDeclarations = new ArrayList<TypeDeclaration>(1);

        if (this.scanner.peek().is(Token.Kind.AT)
            && this.scanner.peekNextButOne().is(Token.Kind.IDENTIFIER))
        {
            List<Annotation> annotations = this.parseAnnotationList();

            if (this.scanner.peek().is(Token.Kind.KEYWORD_PACKAGE)
                && this.scanner.peekNextButOne().is(Token.Kind.IDENTIFIER))
            {
                packageDeclaration = this.parsePackageDeclaration(annotations);
                while (this.scanner.peek().is(Token.Kind.KEYWORD_IMPORT))
                {
                    imports.add(this.parseImportDeclaration());
                }
            } else {
                throw this.error("TODO: parse annotated type declaration");
            }
        } else {
            if (this.scanner.peek().is(Token.Kind.KEYWORD_PACKAGE)
                && this.scanner.peekNextButOne().is(Token.Kind.IDENTIFIER))
            {
                packageDeclaration = this.parsePackageDeclaration(
                        Collections.<Annotation>emptyList()
                );
            }
            while (this.scanner.peek().is(Token.Kind.KEYWORD_IMPORT))
            {
                imports.add(this.parseImportDeclaration());
            }
        }
        while (!this.scanner.peek().is(Token.Kind.EOF))
        {
            throw this.error("TODO: parse type declaration");
        }

        return new CompilationUnit(
                packageDeclaration,
                imports,
                typeDeclarations
        );
    }

    private PackageDeclaration parsePackageDeclaration(List<Annotation> annotations)
        throws ParserException, IOException
    {
        SourcePosition position = this.scanner.getSourcePosition();
        Name packageName;

        this.scanner.expect(Token.Kind.KEYWORD_PACKAGE);
        packageName = this.parseQualifiedIdentifier();
        this.scanner.expect(Token.Kind.SEMICOLON);

        return new PackageDeclaration(position, annotations, packageName);
    }

    private ImportDeclaration parseImportDeclaration()
        throws ParserException, IOException
    {
        SourcePosition position = this.scanner.getSourcePosition();
        boolean _static;
        Name className;
        boolean onDemand;

        this.scanner.expect(Token.Kind.KEYWORD_IMPORT);
        if (this.scanner.peek().is(Token.Kind.KEYWORD_STATIC))
        {
            this.scanner.read();
            _static = true;
        } else {
            _static = false;
        }
        className = this.parseQualifiedIdentifier();
        if (this.scanner.peek().is(Token.Kind.DOT)
            && this.scanner.peekNextButOne().is(Token.Kind.MUL))
        {
            this.scanner.read();
            this.scanner.read();
            onDemand = true;
        } else {
            onDemand = false;
        }
        this.scanner.expect(Token.Kind.SEMICOLON);

        return new ImportDeclaration(
                position,
                _static,
                className,
                onDemand
        );
    }

    private TypeDeclaration parseTypeDeclaration(Modifiers modifiers)
        throws ParserException, IOException
    {
        Token token = this.scanner.peek();

        if (token.is(Token.Kind.KEYWORD_CLASS))
        {
            throw this.error("TODO: parse class declaration");
        }
        else if (token.is(Token.Kind.KEYWORD_INTERFACE))
        {
            throw this.error("TODO: parse interface declaration");
        }
        else if (token.is(Token.Kind.KEYWORD_ENUM))
        {
            throw this.error("TODO: parse enum declaration");
        }
        else if (token.is(Token.Kind.AT)
                && this.scanner.peekNextButOne().is(Token.Kind.KEYWORD_INTERFACE))
        {
            throw this.error("TODO: parse annotation declaration");
        }

        throw this.error("unexpected %s; missing type declaration", token);
    }

    private List<Annotation> parseAnnotationList()
        throws ParserException, IOException
    {
        List<Annotation> list = null;

        while (this.scanner.peek().is(Token.Kind.AT)
                && this.scanner.peekNextButOne().is(Token.Kind.IDENTIFIER))
        {
            Annotation annotation = this.parseAnnotation();

            if (list == null)
            {
                list = new ArrayList<Annotation>(3);
            }
            list.add(annotation);
        }
        if (list == null)
        {
            return Collections.emptyList();
        } else {
            return list;
        }
    }

    private Annotation parseAnnotation()
        throws ParserException, IOException
    {
        this.scanner.expect(Token.Kind.AT);

        throw this.error("TODO: parse annotation");
    }

    /**
     * <pre>
     *   ElementValue:
     *     ConditionalExpression
     *     ElementValueArrayInitializer
     *     Annotation
     * </pre>
     */
    private ElementValue parseElementValue()
        throws ParserException, IOException
    {
        if (this.scanner.peek().is(Token.Kind.LBRACE))
        {
            return this.parseElementValueArrayInitializer();
        }
        else if (this.scanner.peek().is(Token.Kind.AT))
        {
            return this.parseAnnotation();
        } else {
            return this.toExpression(this.parseConditionalExpression());
        }
    }

    /**
     * <pre>
     *   ElementValueArrayInitializer:
     *     { [ElementValueList] [,] }
     *
     *   ElementValueList:
     *     ElementValue {, ElementValue}
     * </pre>
     */
    private ElementValueArrayInitializer parseElementValueArrayInitializer()
        throws ParserException, IOException
    {
        List<ElementValue> list = null;

        this.scanner.expect(Token.Kind.LBRACE);
        while (!this.scanner.peek().is(Token.Kind.RBRACE))
        {
            ElementValue value = this.parseElementValue();

            if (list == null)
            {
                list = new ArrayList<ElementValue>(3);
            }
            list.add(value);
            if (this.scanner.peek().is(Token.Kind.COMMA))
            {
                this.scanner.read();
            } else {
                break;
            }
        }
        this.scanner.expect(Token.Kind.RBRACE);
        if (list == null)
        {
            list = Collections.emptyList();
        }

        return new ElementValueArrayInitializer(list);
    }

    private Name parseQualifiedIdentifier()
        throws ParserException, IOException
    {
        throw this.error("TODO: parse qualified identifier");
    }

    private Atom parseExpression()
        throws ParserException, IOException
    {
        throw this.error("TODO: parse expression");
    }

    private Atom parseConditionalExpression()
        throws ParserException, IOException
    {
        Atom atom = this.parseConditionalOrExpression();

        if (this.scanner.peek().is(Token.Kind.CONDITIONAL))
        {
            Expression condition = this.toExpression(atom);
            Expression trueExpression;
            Expression falseExpression;

            this.scanner.read();
            trueExpression = this.toExpression(this.parseExpression());
            this.scanner.expect(Token.Kind.COLON);
            falseExpression = this.toExpression(this.parseConditionalExpression());

            return new ConditionalExpression(
                    condition.getSourcePosition(),
                    condition,
                    trueExpression,
                    falseExpression
            );
        }

        return atom;
    }

    private Atom parseConditionalOrExpression()
        throws ParserException, IOException
    {
        throw this.error("TODO: parse conditional or expression");
    }

    private Expression toExpression(Atom atom)
        throws ParserException
    {
        if (atom instanceof Expression)
        {
            return (Expression) atom;
        }

        throw this.error("unexpected %s; missing expression", atom);
    }

    private ParserException error(String message, Object... args)
    {
        if (args != null && args.length > 0)
        {
            message = String.format(message, (Object[]) args);
        }

        return new ParserException(this.scanner.getSourcePosition(), message);
    }
}
