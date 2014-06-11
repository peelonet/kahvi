package net.peelo.kahvi.compiler.parser;

import net.peelo.kahvi.compiler.ast.CompilationUnit;
import net.peelo.kahvi.compiler.ast.SourcePosition;
import net.peelo.kahvi.compiler.ast.annotation.*;
import net.peelo.kahvi.compiler.ast.declaration.*;
import net.peelo.kahvi.compiler.ast.expression.*;
import net.peelo.kahvi.compiler.ast.statement.*;
import net.peelo.kahvi.compiler.util.Name;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Parser implements SourcePosition
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
    public int getLineNumber()
    {
        return this.scanner.getLineNumber();
    }

    @Override
    public int getColumnNumber()
    {
        return this.scanner.getColumnNumber();
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

        return null; // TODO
    }

    private Name parseQualifiedIdentifier()
        throws ParserException, IOException
    {
        return null; // TODO
    }

    private ParserException error(String message, Object... args)
    {
        if (args != null && args.length > 0)
        {
            message = String.format(message, (Object[]) args);
        }

        return new ParserException(
                message,
                this.scanner.getLineNumber(),
                this.scanner.getColumnNumber()
        );
    }
}
