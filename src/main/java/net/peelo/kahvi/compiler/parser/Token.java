package net.peelo.kahvi.compiler.parser;

import net.peelo.kahvi.compiler.ast.SourcePosition;

public final class Token implements SourcePosition
{
    public enum Kind
    {
        EOF("end of input"),
        IDENTIFIER("identifier");

        private final String description;

        private Kind(String description)
        {
            this.description = description;
        }

        @Override
        public String toString()
        {
            return this.description;
        }
    }

    private final Kind kind;
    private final String text;
    private final int lineNumber;
    private final int columnNumber;

    public Token(Kind kind,
                 String text,
                 int lineNumber,
                 int columnNumber)
    {
        this.kind = kind;
        this.text = text;
        this.lineNumber = lineNumber;
        this.columnNumber = columnNumber;
    }

    public Kind getKind()
    {
        return this.kind;
    }

    public String getText()
    {
        return this.text;
    }

    @Override
    public int getLineNumber()
    {
        return this.lineNumber;
    }

    @Override
    public int getColumnNumber()
    {
        return this.columnNumber;
    }
}
