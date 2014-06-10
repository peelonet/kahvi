package net.peelo.kahvi.compiler.parser;

import net.peelo.kahvi.compiler.ast.SourcePosition;

import java.io.IOException;
import java.io.Reader;

public final class Scanner implements SourcePosition
{
    private final Reader input;
    private int nextChar;
    private int lineNumber;
    private int columnNumber;
    private boolean crLfPending;
    private Token nextToken;
    private Token nextButOneToken;

    public Scanner(Reader input, int lineNumber, int columnNumber)
    {
        this.input = input;
        this.lineNumber = lineNumber;
        this.columnNumber = columnNumber;
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

    /**
     * Reads next token from the input.
     */
    public Token read()
        throws ParserException, IOException
    {
        Token token = this.nextToken;

        if (token == null)
        {
            if (this.nextButOneToken == null)
            {
                token = this.produce();
            } else {
                token = this.nextButOneToken;
                this.nextButOneToken = null;
            }
        }
        else if (this.nextButOneToken == null)
        {
            this.nextToken = null;
        } else {
            this.nextToken = this.nextButOneToken;
            this.nextButOneToken = null;
        }

        return token;
    }

    /**
     * Peeks the next token but doesn't remove it from the input.
     */
    public Token peek()
        throws ParserException, IOException
    {
        if (this.nextToken == null)
        {
            if (this.nextButOneToken == null)
            {
                this.nextToken = this.produce();
            } else {
                this.nextToken = this.nextButOneToken;
                this.nextButOneToken = null;
            }
        }

        return this.nextToken;
    }

    private Token produce()
        throws IOException
    {
        while (Character.isWhitespace(this.nextChar))
        {
            this.advance();
        }
        if (this.nextChar < 0)
        {
            return this.token(Token.Kind.EOF);
        }
        switch (this.nextChar)
        {
            case '(':
                this.advance();
                return this.token(Token.Kind.LPAREN);

            case ')':
                this.advance();
                return this.token(Token.Kind.RPAREN);

            case '[':
                this.advance();
                return this.token(Token.Kind.LBRACK);

            case ']':
                this.advance();
                return this.token(Token.Kind.RBRACK);

            case '{':
                this.advance();
                return this.token(Token.Kind.LBRACE);

            case '}':
                this.advance();
                return this.token(Token.Kind.RBRACE);

            case ',':
                this.advance();
                return this.token(Token.Kind.COMMA);

            case ':':
                this.advance();
                return this.token(Token.Kind.COLON);

            case ';':
                this.advance();
                return this.token(Token.Kind.SEMICOLON);

            case '~':
                this.advance();
                return this.token(Token.Kind.BIT_NOT);

            case '@':
                this.advance();
                return this.token(Token.Kind.AT);

            case '.':
                this.advance();
                if (this.nextChar == '.')
                {
                    this.advance();
                    if (this.nextChar != '.')
                    {
                        throw this.error("unexpected `..' in input");
                    }
                    this.advance();

                    return this.token(Token.Kind.DOT_DOT_DOT);
                } else {
                    return this.token(Token.Kind.DOT);
                }

            case '?':
                this.advance();
                if (this.nextChar == ':')
                {
                    this.advance();
                    
                    return this.token(Token.Kind.ELVIS);
                } else {
                    return this.token(Token.Kind.CONDITIONAL);
                }

            case '+':
                this.advance();
                if (this.nextChar == '+')
                {
                    this.advance();

                    return this.token(Token.Kind.INC);
                }
                else if (this.nextChar == '=')
                {
                    this.advance();

                    return this.token(Token.Kind.ASSIGN_ADD);
                } else {
                    return this.token(Token.Kind.ADD);
                }

            case '-':
                this.advance();
                if (this.nextChar == '-')
                {
                    this.advance();

                    return this.token(Token.Kind.DEC);
                }
                else if (this.nextChar == '=')
                {
                    this.advance();

                    return this.token(Token.Kind.ASSIGN_SUB);
                } else {
                    return this.token(Token.Kind.SUB);
                }

            case '*':
                this.advance();
                if (this.nextChar == '=')
                {
                    this.advance();

                    return this.token(Token.Kind.ASSIGN_MUL);
                } else {
                    return this.token(Token.Kind.MUL);
                }

            // TODO: /
            // TODO: %
            // TODO: &
            // TODO: |
            // TODO: ^
            // TODO: <
            // TODO: >
            // TODO: =
            // TODO: !
            // TODO: '
            // TODO: "
            // TODO: 0-9
        }
        if (Character.isJavaIdentifierStart(this.nextChar))
        {
            // TODO: scan identifier
        }

        throw this.error("invalid character input `%c' (U+%04X)",
                         this.nextChar,
                         this.nextChar);
    }

    /**
     * Reads one character and stores it in {@code #nextChar}.
     */
    private void advance()
        throws IOException
    {
        this.nextChar = this.input.read();
        if (this.nextChar == '\r')
        {
            ++this.lineNumber;
            this.columnNumber = 0;
            this.crLfPending = true;
        }
        else if (this.nextChar == '\n')
        {
            if (this.crLfPending)
            {
                this.crLfPending = false;
            } else {
                ++this.lineNumber;
                this.columnNumber = 0;
            }
        } else {
            if (this.crLfPending)
            {
                this.crLfPending = false;
            }
            ++this.columnNumber;
        }
    }

    private Token token(Token.Kind kind)
    {
        return new Token(kind, null, this.lineNumber, this.columnNumber);
    }

    private ParserException error(String message, Object... args)
    {
        if (args != null && args.length > 0)
        {
            message = String.format(message, (Object[]) args);
        }

        return new ParserException(message, this.lineNumber, this.columnNumber);
    }
}
