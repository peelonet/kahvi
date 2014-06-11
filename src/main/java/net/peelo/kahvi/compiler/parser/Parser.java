package net.peelo.kahvi.compiler.parser;

import net.peelo.kahvi.compiler.ast.CompilationUnit;
import net.peelo.kahvi.compiler.ast.SourcePosition;
import net.peelo.kahvi.compiler.ast.declaration.*;
import net.peelo.kahvi.compiler.ast.expression.*;
import net.peelo.kahvi.compiler.ast.statement.*;

import java.io.IOException;

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
}
