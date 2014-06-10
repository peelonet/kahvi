package net.peelo.kahvi.compiler.parser;

import net.peelo.kahvi.compiler.ast.SourcePosition;

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
}
