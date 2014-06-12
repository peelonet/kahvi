package net.peelo.kahvi.compiler.parser;

import net.peelo.kahvi.compiler.util.SourceLocatable;
import net.peelo.kahvi.compiler.util.SourcePosition;

import java.io.Serializable;

public final class Token implements SourceLocatable, Serializable
{
    private static final long serialVersionUID = 1L;

    public enum Kind
    {
        EOF("end of input"),
        IDENTIFIER("identifier"),

        // Separators
        LPAREN("`('"),
        RPAREN("`)'"),
        LBRACK("`['"),
        RBRACK("`]'"),
        LBRACE("`{'"),
        RBRACE("`}'"),
        DOT("`.'"),
        DOT_DOT_DOT("`...'"),
        COMMA("`,'"),
        COLON("`:'"),
        SEMICOLON("`;'"),
        CONDITIONAL("`?'"),
        AT("`@'"),
        ARROW("`=>'"),

        // Operators
        ADD("`+'"),
        SUB("`-'"),
        MUL("`*'"),
        DIV("`/'"),
        MOD("`%'"),
        AND("`&&'"),
        OR("`||'"),
        NOT("`!'"),
        BIT_AND("`&'"),
        BIT_OR("`|'"),
        BIT_XOR("`^'"),
        BIT_NOT("`~'"),
        LSH("`<<'"),
        RSH("`>>'"),
        RSH2("`>>>'"),
        EQ("`=='"),
        NE("`!='"),
        LT("`<'"),
        GT("`>'"),
        LTE("`<='"),
        GTE("`>='"),
        INC("`++'"),
        DEC("`--'"),

        // Assignment operators
        ASSIGN("`='"),
        ASSIGN_ADD("`+='"),
        ASSIGN_SUB("`-='"),
        ASSIGN_MUL("`*='"),
        ASSIGN_DIV("`/='"),
        ASSIGN_MOD("`%='"),
        ASSIGN_BIT_AND("`&='"),
        ASSIGN_BIT_OR("`|='"),
        ASSIGN_BIT_XOR("`^='"),
        ASSIGN_LSH("`<<'"),
        ASSIGN_RSH("`>>'"),
        ASSIGN_RSH2("`>>>'"),

        // Keywords
        KW_ABSTRACT("`abstract'"),
        KW_ASSERT("`assert'"),
        KW_BOOLEAN("`boolean'"),
        KW_BREAK("`break'"),
        KW_BYTE("`byte'"),
        KW_CASE("`case'"),
        KW_CATCH("`catch'"),
        KW_CHAR("`char'"),
        KW_CLASS("`class'"),
        KW_CONTINUE("`continue'"),
        KW_DEFAULT("`default'"),
        KW_DO("`do'"),
        KW_DOUBLE("`double'"),
        KW_ELSE("`else'"),
        KW_ENUM("`enum'"),
        KW_EXTENDS("`extends'"),
        KW_FALSE("`false'"),
        KW_FINAL("`final'"),
        KW_FINALLY("`finally'"),
        KW_FLOAT("`float'"),
        KW_FOR("`for'"),
        KW_IF("`if'"),
        KW_IMPLEMENTS("`implements'"),
        KW_IMPORT("`import'"),
        KW_INSTANCEOF("`instanceof'"),
        KW_INT("`int'"),
        KW_INTERFACE("`interface'"),
        KW_LONG("`long'"),
        KW_NATIVE("`native'"),
        KW_NEW("`new'"),
        KW_NULL("`null'"),
        KW_PACKAGE("`package'"),
        KW_PRIVATE("`private'"),
        KW_PROTECTED("`protected'"),
        KW_PUBLIC("`public'"),
        KW_RETURN("`return'"),
        KW_SHORT("`short'"),
        KW_STATIC("`static'"),
        KW_STRICTFP("`strictfp'"),
        KW_SUPER("`super'"),
        KW_SWITCH("`switch'"),
        KW_SYNCHRONIZED("`synchronized'"),
        KW_THIS("`this'"),
        KW_THROW("`throw'"),
        KW_THROWS("`throws'"),
        KW_TRANSIENT("`transient'"),
        KW_TRUE("`true'"),
        KW_TRY("`try'"),
        KW_VOID("`void'"),
        KW_VOLATILE("`volatile'"),
        KW_WHILE("`while'");

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
    private transient SourcePosition position;

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
    public synchronized SourcePosition getSourcePosition()
    {
        if (this.position == null)
        {
            this.position = new SourcePosition(
                    this.lineNumber,
                    this.columnNumber
            );
        }

        return this.position;
    }

    @Override
    public String toString()
    {
        return this.kind.toString();
    }
}
