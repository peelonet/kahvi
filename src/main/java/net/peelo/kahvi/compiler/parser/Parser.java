package net.peelo.kahvi.compiler.parser;

import net.peelo.kahvi.compiler.ast.Atom;
import net.peelo.kahvi.compiler.ast.CompilationUnit;
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
        return null; // TODO
    }

    private List<Annotation> parseAnnotationList()
        throws ParserException, IOException
    {
        List<Annotation> list = null;

        while (this.scanner.peek().is(Token.Kind.AT))
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
