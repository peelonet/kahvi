package net.peelo.kahvi.compiler.parser;

import net.peelo.kahvi.compiler.ast.SourcePosition;

public final class Token implements SourcePosition
{
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
        ELVIS("`?:'"),

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
        KEYWORD_ABSTRACT("`abstract'"),
        KEYWORD_ASSERT("`assert'"),
        KEYWORD_BOOLEAN("`boolean'"),
        KEYWORD_BREAK("`break'"),
        KEYWORD_BYTE("`byte'"),
        KEYWORD_CASE("`case'"),
        KEYWORD_CATCH("`catch'"),
        KEYWORD_CHAR("`char'"),
        KEYWORD_CLASS("`class'"),
        KEYWORD_CONTINUE("`continue'"),
        KEYWORD_DEFAULT("`default'"),
        KEYWORD_DO("`do'"),
        KEYWORD_DOUBLE("`double'"),
        KEYWORD_ELSE("`else'"),
        KEYWORD_ENUM("`enum'"),
        KEYWORD_EXTENDS("`extends'"),
        KEYWORD_FALSE("`false'"),
        KEYWORD_FINAL("`final'"),
        KEYWORD_FINALLY("`finally'"),
        KEYWORD_FLOAT("`float'"),
        KEYWORD_FOR("`for'"),
        KEYWORD_IF("`if'"),
        KEYWORD_IMPLEMENTS("`implements'"),
        KEYWORD_IMPORT("`import'"),
        KEYWORD_INSTANCEOF("`instanceof'"),
        KEYWORD_INT("`int'"),
        KEYWORD_INTERFACE("`interface'"),
        KEYWORD_LONG("`long'"),
        KEYWORD_NATIVE("`native'"),
        KEYWORD_NEW("`new'"),
        KEYWORD_NULL("`null'"),
        KEYWORD_PACKAGE("`package'"),
        KEYWORD_PRIVATE("`private'"),
        KEYWORD_PROTECTED("`protected'"),
        KEYWORD_PUBLIC("`public'"),
        KEYWORD_RETURN("`return'"),
        KEYWORD_SHORT("`short'"),
        KEYWORD_STATIC("`static'"),
        KEYWORD_STRICTFP("`strictfp'"),
        KEYWORD_SUPER("`super'"),
        KEYWORD_SWITCH("`switch'"),
        KEYWORD_SYNCHRONIZED("`synchronized'"),
        KEYWORD_THIS("`this'"),
        KEYWORD_THROW("`throw'"),
        KEYWORD_THROWS("`throws'"),
        KEYWORD_TRANSIENT("`transient'"),
        KEYWORD_TRUE("`true'"),
        KEYWORD_TRY("`try'"),
        KEYWORD_VOID("`void'"),
        KEYWORD_VOLATILE("`volatile'"),
        KEYWORD_WHILE("`while'");

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
