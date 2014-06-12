package net.peelo.kahvi.compiler.parser;

import net.peelo.kahvi.compiler.util.SourceLocatable;
import net.peelo.kahvi.compiler.util.SourcePosition;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

public final class Scanner implements SourceLocatable
{
    private final File file;
    private Reader input;
    private int lineNumber;
    private int columnNumber;
    private int nextTokenLineNumber;
    private int nextTokenColumnNumber;
    private boolean crLfPending;
    private int pushbackChar;

    public Scanner(Reader input)
    {
        this(null, input, 1, 0);
    }

    public Scanner(File file, Reader input)
    {
        this(file, input, 1, 0);
    }

    public Scanner(Reader input,
                   int initialLineNumber,
                   int initialColumnNumber)
    {
        this(null, input, initialLineNumber, initialColumnNumber);
    }

    public Scanner(File file,
                   Reader input,
                   int initialLineNumber,
                   int initialColumnNumber)
    {
        this.file = file;
        this.input = input;
        this.lineNumber = initialLineNumber;
        this.columnNumber = initialColumnNumber;
    }

    @Override
    public SourcePosition getSourcePosition()
    {
        return new SourcePosition(
                this.file,
                this.lineNumber,
                this.columnNumber
        );
    }

    /**
     * Scans next token from the input.
     */
    public Token scan()
        throws IOException
    {
        for (;;)
        {
            int c = this.advance();

            if (c < 0)
            {
                return this.token(Token.Kind.EOF);
            }
            if (Character.isWhitespace(c))
            {
                continue;
            }
            this.nextTokenLineNumber = this.lineNumber;
            this.nextTokenColumnNumber = this.columnNumber;
            switch (c)
            {
                case '(':
                    return this.token(Token.Kind.LPAREN);

                case ')':
                    return this.token(Token.Kind.RPAREN);

                case '[':
                    return this.token(Token.Kind.LBRACK);

                case ']':
                    return this.token(Token.Kind.RBRACK);

                case '{':
                    return this.token(Token.Kind.LBRACE);

                case '}':
                    return this.token(Token.Kind.RBRACE);

                case ',':
                    return this.token(Token.Kind.COMMA);

                case ':':
                    return this.token(Token.Kind.COLON);

                case ';':
                    return this.token(Token.Kind.SEMICOLON);

                case '~':
                    return this.token(Token.Kind.BIT_NOT);

                case '@':
                    return this.token(Token.Kind.AT);

                case '?':
                    return this.token(Token.Kind.CONDITIONAL);

                case '.':
                    if (this.advance('.'))
                    {
                        if (this.advance('.'))
                        {
                            return this.token(Token.Kind.DOT_DOT_DOT);
                        } else {
                            throw this.error("unexpected `..' in input");
                        }
                    } else {
                        return this.token(Token.Kind.DOT);
                    }

                case '+':
                    if (this.advance('+'))
                    {
                        return this.token(Token.Kind.INC);
                    }
                    else if (this.advance('='))
                    {
                        return this.token(Token.Kind.ASSIGN_ADD);
                    } else {
                        return this.token(Token.Kind.ADD);
                    }

                case '-':
                    if (this.advance('-'))
                    {
                        return this.token(Token.Kind.DEC);
                    }
                    else if (this.advance('='))
                    {
                        return this.token(Token.Kind.ASSIGN_SUB);
                    } else {
                        return this.token(Token.Kind.SUB);
                    }

                case '*':
                    if (this.advance('='))
                    {
                        return this.token(Token.Kind.ASSIGN_MUL);
                    } else {
                        return this.token(Token.Kind.MUL);
                    }

                case '/':
                    if (this.advance('/'))
                    {
                        throw this.error("TODO: skip single line comments");
                    }
                    else if (this.advance('*'))
                    {
                        throw this.error("TODO: skip multi line comments");
                    }
                    else if (this.advance('='))
                    {
                        return this.token(Token.Kind.ASSIGN_DIV);
                    } else {
                        return this.token(Token.Kind.DIV);
                    }

                case '%':
                    if (this.advance('='))
                    {
                        return this.token(Token.Kind.ASSIGN_MOD);
                    } else {
                        return this.token(Token.Kind.MOD);
                    }

                case '&':
                    if (this.advance('&'))
                    {
                        return this.token(Token.Kind.AND);
                    }
                    else if (this.advance('='))
                    {
                        return this.token(Token.Kind.ASSIGN_BIT_AND);
                    } else {
                        return this.token(Token.Kind.BIT_AND);
                    }

                case '|':
                    if (this.advance('|'))
                    {
                        return this.token(Token.Kind.OR);
                    }
                    else if (this.advance('='))
                    {
                        return this.token(Token.Kind.ASSIGN_BIT_OR);
                    } else {
                        return this.token(Token.Kind.BIT_OR);
                    }

                case '^':
                    if (this.advance('='))
                    {
                        return this.token(Token.Kind.ASSIGN_BIT_XOR);
                    } else {
                        return this.token(Token.Kind.BIT_XOR);
                    }

                case '<':
                    if (this.advance('<'))
                    {
                        if (this.advance('='))
                        {
                            return this.token(Token.Kind.ASSIGN_LSH);
                        } else {
                            return this.token(Token.Kind.LSH);
                        }
                    }
                    else if (this.advance('='))
                    {
                        return this.token(Token.Kind.LTE);
                    } else {
                        return this.token(Token.Kind.LT);
                    }

                case '>':
                    if (this.advance('>'))
                    {
                        if (this.advance('>'))
                        {
                            if (this.advance('='))
                            {
                                return this.token(Token.Kind.ASSIGN_RSH2);
                            } else {
                                return this.token(Token.Kind.RSH2);
                            }
                        }
                        else if (this.advance('='))
                        {
                            return this.token(Token.Kind.ASSIGN_RSH);
                        } else {
                            return this.token(Token.Kind.RSH);
                        }
                    }
                    else if (this.advance('='))
                    {
                        return this.token(Token.Kind.GTE);
                    } else {
                        return this.token(Token.Kind.GT);
                    }

                case '=':
                    if (this.advance('='))
                    {
                        return this.token(Token.Kind.EQ);
                    } else {
                        return this.token(Token.Kind.ASSIGN);
                    }

                case '!':
                    if (this.advance('='))
                    {
                        return this.token(Token.Kind.NE);
                    } else {
                        return this.token(Token.Kind.NOT);
                    }

                case '0': case '1': case '2': case '3': case '4':
                case '5': case '6': case '7': case '8': case '9':
                    throw this.error("TODO: number literals");

                case '"':
                    throw this.error("TODO: string literals");

                case '\'':
                    throw this.error("TODO: char literals");

                default:
                    if (Character.isJavaIdentifierStart(c))
                    {
                        StringBuilder buffer = new StringBuilder();

                        do
                        {
                            buffer.append((char) c);
                            c = this.advance();
                        }
                        while (Character.isJavaIdentifierPart(c));
                        this.pushbackChar = c;

                        return this.identifierOrKeyword(buffer.toString());
                    } else {
                        throw this.error(
                                "invalid character input: `%c' (U+%04X)",
                                c, c
                        );
                    }
            }
        }
    }

    private int advance()
        throws IOException
    {
        int c;

        if (this.pushbackChar == 0)
        {
            if (this.input == null)
            {
                c = -1;
            } else {
                if ((c = this.input.read()) < 0)
                {
                    this.input = null;
                }
                else if (c == '\r')
                {
                    ++this.lineNumber;
                    this.columnNumber = 0;
                    this.crLfPending = true;
                }
                else if (c == '\n')
                {
                    if (this.crLfPending)
                    {
                        this.crLfPending = false;
                    } else {
                        ++this.lineNumber;
                        this.columnNumber = 0;
                    }
                } else {
                    this.crLfPending = false;
                    ++this.columnNumber;
                }
            }
        } else {
            c = this.pushbackChar;
            this.pushbackChar = 0;
        }

        return c;
    }

    private boolean advance(int expected)
        throws IOException
    {
        int c = this.advance();

        if (c == expected)
        {
            return true;
        }
        this.pushbackChar = c;

        return false;
    }

    private Token token(Token.Kind kind)
    {
        return this.token(kind, null);
    }

    private Token token(Token.Kind kind, String text)
    {
        return new Token(
                kind,
                text,
                this.file,
                this.nextTokenLineNumber,
                this.nextTokenColumnNumber
        );
    }

    private ParserException error(String message, Object... args)
    {
        return this.error(null, message, (Object[]) args);
    }

    private ParserException error(SourceLocatable location,
                                  String message,
                                  Object... args)
    {
        if (args != null && args.length > 0)
        {
            message = String.format(message, (Object[]) args);
        }

        return new ParserException(
                location == null ? this.getSourcePosition()
                                 : location.getSourcePosition(),
                message
        );
    }

    private static final Map<String, Token.Kind> KEYWORD_MAP;

    static
    {
        KEYWORD_MAP = new HashMap<String, Token.Kind>();
        KEYWORD_MAP.put("abstract", Token.Kind.KW_ABSTRACT);
        KEYWORD_MAP.put("assert", Token.Kind.KW_ASSERT);
        KEYWORD_MAP.put("boolean", Token.Kind.KW_BOOLEAN);
        KEYWORD_MAP.put("break", Token.Kind.KW_BREAK);
        KEYWORD_MAP.put("byte", Token.Kind.KW_BYTE);
        KEYWORD_MAP.put("case", Token.Kind.KW_CASE);
        KEYWORD_MAP.put("catch", Token.Kind.KW_CATCH);
        KEYWORD_MAP.put("char", Token.Kind.KW_CHAR);
        KEYWORD_MAP.put("class", Token.Kind.KW_CLASS);
        KEYWORD_MAP.put("continue", Token.Kind.KW_CONTINUE);
        KEYWORD_MAP.put("default", Token.Kind.KW_DEFAULT);
        KEYWORD_MAP.put("do", Token.Kind.KW_DO);
        KEYWORD_MAP.put("double", Token.Kind.KW_DOUBLE);
        KEYWORD_MAP.put("else", Token.Kind.KW_ELSE);
        KEYWORD_MAP.put("enum", Token.Kind.KW_ENUM);
        KEYWORD_MAP.put("extends", Token.Kind.KW_EXTENDS);
        KEYWORD_MAP.put("false", Token.Kind.KW_FALSE);
        KEYWORD_MAP.put("final", Token.Kind.KW_FINAL);
        KEYWORD_MAP.put("finally", Token.Kind.KW_FINALLY);
        KEYWORD_MAP.put("float", Token.Kind.KW_FLOAT);
        KEYWORD_MAP.put("for", Token.Kind.KW_FOR);
        KEYWORD_MAP.put("if", Token.Kind.KW_IF);
        KEYWORD_MAP.put("implements", Token.Kind.KW_IMPLEMENTS);
        KEYWORD_MAP.put("import", Token.Kind.KW_IMPORT);
        KEYWORD_MAP.put("instanceof", Token.Kind.KW_INSTANCEOF);
        KEYWORD_MAP.put("int", Token.Kind.KW_INT);
        KEYWORD_MAP.put("interface", Token.Kind.KW_INTERFACE);
        KEYWORD_MAP.put("long", Token.Kind.KW_LONG);
        KEYWORD_MAP.put("native", Token.Kind.KW_NATIVE);
        KEYWORD_MAP.put("new", Token.Kind.KW_NEW);
        KEYWORD_MAP.put("null", Token.Kind.KW_NULL);
        KEYWORD_MAP.put("package", Token.Kind.KW_PACKAGE);
        KEYWORD_MAP.put("private", Token.Kind.KW_PRIVATE);
        KEYWORD_MAP.put("protected", Token.Kind.KW_PROTECTED);
        KEYWORD_MAP.put("public", Token.Kind.KW_PUBLIC);
        KEYWORD_MAP.put("return", Token.Kind.KW_RETURN);
        KEYWORD_MAP.put("short", Token.Kind.KW_SHORT);
        KEYWORD_MAP.put("static", Token.Kind.KW_STATIC);
        KEYWORD_MAP.put("strictfp", Token.Kind.KW_STRICTFP);
        KEYWORD_MAP.put("super", Token.Kind.KW_SUPER);
        KEYWORD_MAP.put("switch", Token.Kind.KW_SWITCH);
        KEYWORD_MAP.put("synchronized", Token.Kind.KW_SYNCHRONIZED);
        KEYWORD_MAP.put("this", Token.Kind.KW_THIS);
        KEYWORD_MAP.put("throw", Token.Kind.KW_THROW);
        KEYWORD_MAP.put("throws", Token.Kind.KW_THROWS);
        KEYWORD_MAP.put("transient", Token.Kind.KW_TRANSIENT);
        KEYWORD_MAP.put("true", Token.Kind.KW_TRUE);
        KEYWORD_MAP.put("try", Token.Kind.KW_TRY);
        KEYWORD_MAP.put("void", Token.Kind.KW_VOID);
        KEYWORD_MAP.put("volatile", Token.Kind.KW_VOLATILE);
        KEYWORD_MAP.put("while", Token.Kind.KW_WHILE);
    }

    private Token identifierOrKeyword(String text)
    {
        Token.Kind kind = KEYWORD_MAP.get(text);

        if (kind == null)
        {
            return this.token(Token.Kind.IDENTIFIER, text);
        } else {
            return this.token(kind);
        }
    }
}
