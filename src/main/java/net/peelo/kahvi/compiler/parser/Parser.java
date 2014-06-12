package net.peelo.kahvi.compiler.parser;

import net.peelo.kahvi.compiler.ast.Atom;
import net.peelo.kahvi.compiler.ast.CompilationUnit;
import net.peelo.kahvi.compiler.ast.Flag;
import net.peelo.kahvi.compiler.ast.Modifiers;
import net.peelo.kahvi.compiler.ast.Visibility;
import net.peelo.kahvi.compiler.ast.annotation.*;
import net.peelo.kahvi.compiler.ast.declaration.*;
import net.peelo.kahvi.compiler.ast.expression.*;
import net.peelo.kahvi.compiler.ast.statement.*;
import net.peelo.kahvi.compiler.ast.type.*;
import net.peelo.kahvi.compiler.lookup.Primitive;
import net.peelo.kahvi.compiler.util.Name;
import net.peelo.kahvi.compiler.util.SourceLocatable;
import net.peelo.kahvi.compiler.util.SourcePosition;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

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
                typeDeclarations.add(this.parseTypeDeclaration(this.parseModifiers(annotations)));
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
            typeDeclarations.add(this.parseTypeDeclaration(this.parseModifiers()));
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

    /**
     * <pre>
     *   Annotation:
     *     NormalAnnotation
     *     MarkerAnnotation
     *     SingleElementAnnotation
     *
     *   NormalAnnotation:
     *     @ TypeName ( [ElementValuePairList] )
     *
     *   MarkerAnnotation:
     *     @ TypeName
     *
     *   SingleElementAnnotation:
     *     @ TypeName ( ElementValue )
     * </pre>
     */
    private Annotation parseAnnotation()
        throws ParserException, IOException
    {
        SourcePosition position = this.scanner.getSourcePosition();
        Name className;

        this.scanner.expect(Token.Kind.AT);
        className = this.parseQualifiedIdentifier();
        if (this.scanner.peek().is(Token.Kind.LPAREN))
        {
            this.scanner.read();
            if (this.scanner.peek().is(Token.Kind.IDENTIFIER)
                && this.scanner.peekNextButOne().is(Token.Kind.ASSIGN))
            {
                throw this.error("TODO: parse normal annotation");
            } else {
                throw this.error("TODO: parse element annotation");
            }
        }

        throw this.error("TODO: parse marker annotation");
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
        Token token = this.scanner.read();
        Name result;

        if (!token.is(Token.Kind.IDENTIFIER))
        {
            throw this.error("unexpected %s; missing identifier", token);
        }
        result = new Name(null, token.getText());
        while (this.scanner.peek().is(Token.Kind.DOT)
                && this.scanner.peekNextButOne().is(Token.Kind.IDENTIFIER))
        {
            this.scanner.read();
            result = new Name(result, this.scanner.read().getText());
        }
        
        return result;
    }

    private Modifiers parseModifiers()
        throws ParserException, IOException
    {
        return this.parseModifiers(this.parseAnnotationList());
    }

    private Modifiers parseModifiers(List<Annotation> annotations)
        throws ParserException, IOException
    {
        Visibility visibility = null;
        Set<Flag> flags = EnumSet.noneOf(Flag.class);

        for (;;)
        {
            if (this.scanner.peekRead(Token.Kind.KEYWORD_PUBLIC))
            {
                if (visibility != null)
                {
                    throw this.error("visibility is both '%s' and 'public'", visibility);
                }
                visibility = Visibility.PUBLIC;
            }
            else if (this.scanner.peekRead(Token.Kind.KEYWORD_PROTECTED))
            {
                if (visibility != null)
                {
                    throw this.error("visibility is both '%s' and 'protected'", visibility);
                }
                visibility = Visibility.PROTECTED;
            }
            else if (this.scanner.peekRead(Token.Kind.KEYWORD_PRIVATE))
            {
                if (visibility != null)
                {
                    throw this.error("visibility is both '%s' and 'private'", visibility);
                }
                visibility = Visibility.PRIVATE;
            }
            else if (this.scanner.peekRead(Token.Kind.KEYWORD_PACKAGE))
            {
                if (visibility != null)
                {
                    throw this.error("visibility is both '%s' and 'package'", visibility);
                }
                visibility = Visibility.PACKAGE;
            } else {
                Flag flag;

                if (this.scanner.peekRead(Token.Kind.KEYWORD_STATIC))
                {
                    flag = Flag.STATIC;
                }
                else if (this.scanner.peekRead(Token.Kind.KEYWORD_ABSTRACT))
                {
                    if (flags.contains(Flag.FINAL))
                    {
                        throw this.error("'abstract' and 'final' are mutually exclusive");
                    }
                    else if (flags.contains(Flag.NATIVE))
                    {
                        throw this.error("'abstract' and 'native' are mutually exclusive");
                    }
                    flag = Flag.ABSTRACT;
                }
                else if (this.scanner.peekRead(Token.Kind.KEYWORD_FINAL))
                {
                    if (flags.contains(Flag.ABSTRACT))
                    {
                        throw this.error("'final' are 'abstract' are mutually exclusive");
                    }
                    flag = Flag.FINAL;
                }
                else if (this.scanner.peekRead(Token.Kind.KEYWORD_NATIVE))
                {
                    if (flags.contains(Flag.ABSTRACT))
                    {
                        throw this.error("'native' and 'abstract' are mutually exclusive");
                    }
                    flag = Flag.NATIVE;
                }
                else if (this.scanner.peekRead(Token.Kind.KEYWORD_SYNCHRONIZED))
                {
                    flag = Flag.SYNCHRONIZED;
                }
                else if (this.scanner.peekRead(Token.Kind.KEYWORD_TRANSIENT))
                {
                    flag = Flag.TRANSIENT;
                }
                else if (this.scanner.peekRead(Token.Kind.KEYWORD_VOLATILE))
                {
                    flag = Flag.VOLATILE;
                }
                else if (this.scanner.peekRead(Token.Kind.KEYWORD_STRICTFP))
                {
                    flag = Flag.STRICTFP;
                } else {
                    return new Modifiers(annotations, visibility, flags);
                }
                if (flags.contains(flag))
                {
                    throw this.error("duplicate modifier: %s", flag);
                }
                flags.add(flag);
            }
        }
    }

    private Atom parseExpression()
        throws ParserException, IOException
    {
        return this.parseAssignmentExpression();
    }

    /**
     * <pre>
     *   AssignmentExpression:
     *     ConditionalExpression
     *     AssignmentExpression = Expression
     *     AssignmentExpression += Expression
     *     AssignmentExpression -= Expression
     *     AssignmentExpression *= Expression
     *     AssignmentExpression /= Expression
     *     AssignmentExpression %= Expression
     *     AssignmentExpression &lt;&lt;= Expression
     *     AssignmentExpression &gt;&gt;= Expression
     *     AssignmentExpression &gt;&gt;&gt;= Expression
     *     AssignmentExpression &amp;= Expression
     *     AssignmentExpression |= Expression
     *     AssignmentExpression ^= Expression
     * </pre>
     */
    private Atom parseAssignmentExpression()
        throws ParserException, IOException
    {
        Atom atom = this.parseConditionalExpression();

        if (this.scanner.peekRead(Token.Kind.ASSIGN))
        {
            return new AssignmentExpression(
                    atom.getSourcePosition(),
                    this.toAssignableExpression(atom),
                    this.toExpression(this.parseExpression())
            );
        }
        else if (this.scanner.peek().is(
                    Token.Kind.ASSIGN_ADD,
                    Token.Kind.ASSIGN_SUB,
                    Token.Kind.ASSIGN_MUL,
                    Token.Kind.ASSIGN_DIV,
                    Token.Kind.ASSIGN_MOD,
                    Token.Kind.ASSIGN_LSH,
                    Token.Kind.ASSIGN_RSH,
                    Token.Kind.ASSIGN_RSH2,
                    Token.Kind.ASSIGN_BIT_AND,
                    Token.Kind.ASSIGN_BIT_OR,
                    Token.Kind.ASSIGN_BIT_XOR))
        {
            Token.Kind kind = this.scanner.read().getKind();

            return new CompoundAssignmentExpression(
                    atom.getSourcePosition(),
                    this.toAssignableExpression(atom),
                    kind == Token.Kind.ASSIGN_ADD ? CompoundAssignmentExpression.Kind.ADD :
                    kind == Token.Kind.ASSIGN_SUB ? CompoundAssignmentExpression.Kind.SUB :
                    kind == Token.Kind.ASSIGN_MUL ? CompoundAssignmentExpression.Kind.MUL :
                    kind == Token.Kind.ASSIGN_DIV ? CompoundAssignmentExpression.Kind.DIV :
                    kind == Token.Kind.ASSIGN_MOD ? CompoundAssignmentExpression.Kind.MOD :
                    kind == Token.Kind.ASSIGN_LSH ? CompoundAssignmentExpression.Kind.LSH :
                    kind == Token.Kind.ASSIGN_RSH ? CompoundAssignmentExpression.Kind.RSH :
                    kind == Token.Kind.ASSIGN_RSH2 ? CompoundAssignmentExpression.Kind.RSH2 :
                    kind == Token.Kind.ASSIGN_BIT_AND ? CompoundAssignmentExpression.Kind.BIT_AND :
                    kind == Token.Kind.ASSIGN_BIT_OR ? CompoundAssignmentExpression.Kind.BIT_OR :
                    CompoundAssignmentExpression.Kind.BIT_XOR,
                    this.toExpression(this.parseExpression())
            );
        }

        return atom;
    }

    private Atom parseConditionalExpression()
        throws ParserException, IOException
    {
        Atom atom = this.parseConditionalOrExpression();

        if (this.scanner.peekRead(Token.Kind.CONDITIONAL))
        {
            Expression condition = this.toExpression(atom);
            Expression trueExpression;
            Expression falseExpression;

            trueExpression = this.toExpression(this.parseExpression());
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

    /**
     * <pre>
     *   ConditionalOrExpression:
     *     ConditionalAndExpression
     *     ConditionalOrExpression || ConditionalAndExpression
     * </pre>
     */
    private Atom parseConditionalOrExpression()
        throws ParserException, IOException
    {
        Atom atom = this.parseConditionalAndExpression();

        while (this.scanner.peekRead(Token.Kind.OR))
        {
            atom = new BinaryExpression(
                    atom.getSourcePosition(),
                    this.toExpression(atom),
                    BinaryExpression.Kind.OR,
                    this.toExpression(this.parseConditionalAndExpression())
            );
        }

        return atom;
    }

    /**
     * <pre>
     *   ConditionalAndExpression:
     *     InclusiveOrExpression
     *     ConditionalAndExpression && InclusiveOrExpression
     * </pre>
     */
    private Atom parseConditionalAndExpression()
        throws ParserException, IOException
    {
        Atom atom = this.parseInclusiveOrExpression();

        while (this.scanner.peekRead(Token.Kind.AND))
        {
            atom = new BinaryExpression(
                    atom.getSourcePosition(),
                    this.toExpression(atom),
                    BinaryExpression.Kind.AND,
                    this.toExpression(this.parseInclusiveOrExpression())
            );
        }

        return atom;
    }

    /**
     * <pre>
     *   InclusiveOrExpression:
     *     ExclusiveOrExpression
     *     InclusiveOrExpression | ExclusiveOrExpression
     * </pre>
     */
    private Atom parseInclusiveOrExpression()
        throws ParserException, IOException
    {
        Atom atom = this.parseExclusiveOrExpression();

        while (this.scanner.peekRead(Token.Kind.BIT_OR))
        {
            atom = new BinaryExpression(
                    atom.getSourcePosition(),
                    this.toExpression(atom),
                    BinaryExpression.Kind.BIT_OR,
                    this.toExpression(this.parseExclusiveOrExpression())
            );
        }

        return atom;
    }

    /**
     * <pre>
     *   ExclusiveOrExpression:
     *     AndExpression
     *     ExclusiveOrExpression ^ AndExpression
     * </pre>
     */
    private Atom parseExclusiveOrExpression()
        throws ParserException, IOException
    {
        Atom atom = this.parseAndExpression();

        while (this.scanner.peekRead(Token.Kind.BIT_XOR))
        {
            atom = new BinaryExpression(
                    atom.getSourcePosition(),
                    this.toExpression(atom),
                    BinaryExpression.Kind.BIT_XOR,
                    this.toExpression(this.parseAndExpression())
            );
        }

        return atom;
    }

    /**
     * <pre>
     *   AndExpression:
     *     EqualityExpression
     *     AndExpression & EqualityExpression
     * </pre>
     */
    private Atom parseAndExpression()
        throws ParserException, IOException
    {
        Atom atom = this.parseEqualityExpression();

        while (this.scanner.peekRead(Token.Kind.BIT_AND))
        {
            atom = new BinaryExpression(
                    atom.getSourcePosition(),
                    this.toExpression(atom),
                    BinaryExpression.Kind.BIT_AND,
                    this.toExpression(this.parseEqualityExpression())
            );
        }

        return atom;
    }

    /**
     * <pre>
     *   EqualityExpression:
     *     RelationalExpression
     *     EqualityExpression == RelationalExpression
     *     EqualityExpression != RelationalExpression
     * </pre>
     */
    private Atom parseEqualityExpression()
        throws ParserException, IOException
    {
        Atom atom = this.parseRelationalExpression();

        while (this.scanner.peek().is(Token.Kind.EQ, Token.Kind.NE))
        {
            Token.Kind kind = this.scanner.read().getKind();

            atom = new BinaryExpression(
                    atom.getSourcePosition(),
                    this.toExpression(atom),
                    kind == Token.Kind.EQ
                        ? BinaryExpression.Kind.EQ
                        : BinaryExpression.Kind.NE,
                    this.toExpression(this.parseRelationalExpression())
            );
        }

        return atom;
    }

    /**
     * <pre>
     *   RelationalExpression:
     *     ShiftExpression
     *     RelationalExpression &lt; ShiftExpression
     *     RelationalExpression &gt; ShiftExpression
     *     RelationalExpression &lt;= ShiftExpression
     *     RelationalExpression &gt;= ShiftExpression
     *     RelationalExpression instanceof ReferenceType
     * </pre>
     */
    private Atom parseRelationalExpression()
        throws ParserException, IOException
    {
        Atom atom = this.parseShiftExpression();

        for (;;)
        {
            if (this.scanner.peek().is(
                        Token.Kind.LT,
                        Token.Kind.GT,
                        Token.Kind.LTE,
                        Token.Kind.GTE))
            {
                Token.Kind kind = this.scanner.read().getKind();

                atom = new BinaryExpression(
                        atom.getSourcePosition(),
                        this.toExpression(atom),
                        kind == Token.Kind.LT ? BinaryExpression.Kind.LT :
                        kind == Token.Kind.GT ? BinaryExpression.Kind.GT :
                        kind == Token.Kind.LTE ? BinaryExpression.Kind.LTE :
                        BinaryExpression.Kind.GTE,
                        this.toExpression(this.parseShiftExpression())
                );
            }
            else if (this.scanner.peek().is(Token.Kind.KEYWORD_INSTANCEOF))
            {
                atom = new InstanceOfExpression(
                        atom.getSourcePosition(),
                        this.toExpression(atom),
                        this.parseReferenceType()
                );
            } else {
                return atom;
            }
        }
    }

    /**
     * <pre>
     *   ShiftExpression:
     *     AdditiveExpression
     *     ShiftExpression &lt;&lt; AdditiveExpression
     *     ShiftExpression &gt;&gt; AdditiveExpression
     *     ShiftExpression &gt;&gt;&gt; AdditiveExpression
     * </pre>
     */
    private Atom parseShiftExpression()
        throws ParserException, IOException
    {
        Atom atom = this.parseAdditiveExpression();

        while (this.scanner.peek().is(
                    Token.Kind.LSH,
                    Token.Kind.RSH,
                    Token.Kind.RSH2))
        {
            Token.Kind kind = this.scanner.read().getKind();

            atom = new BinaryExpression(
                    atom.getSourcePosition(),
                    this.toExpression(atom),
                    kind == Token.Kind.LSH ? BinaryExpression.Kind.LSH :
                    kind == Token.Kind.RSH ? BinaryExpression.Kind.RSH :
                    BinaryExpression.Kind.RSH2,
                    this.toExpression(this.parseAdditiveExpression())
            );
        }

        return atom;
    }

    /**
     * <pre>
     *   AdditiveExpression:
     *     MultiplicativeExpression
     *     AdditiveExpression + MultiplicativeExpression
     *     AdditiveExpression - MultiplicativeExpression
     * </pre>
     */
    private Atom parseAdditiveExpression()
        throws ParserException, IOException
    {
        Atom atom = this.parseMultiplicativeExpression();

        while (this.scanner.peek().is(Token.Kind.ADD, Token.Kind.SUB))
        {
            Token.Kind kind = this.scanner.read().getKind();

            atom = new BinaryExpression(
                    atom.getSourcePosition(),
                    this.toExpression(atom),
                    kind == Token.Kind.ADD ? BinaryExpression.Kind.ADD :
                    BinaryExpression.Kind.SUB,
                    this.toExpression(this.parseMultiplicativeExpression())
            );
        }

        return atom;
    }

    /**
     * <pre>
     *   MultiplicativeExpression:
     *     UnaryExpression
     *     MultiplicativeExpression * UnaryExpression
     *     MultiplicativeExpression / UnaryExpression
     *     MultiplicativeExpression % UnaryExpression
     * </pre>
     */
    private Atom parseMultiplicativeExpression()
        throws ParserException, IOException
    {
        Atom atom = this.parseUnaryExpression();

        while (this.scanner.peek().is(
                    Token.Kind.MUL,
                    Token.Kind.DIV,
                    Token.Kind.MOD))
        {
            Token.Kind kind = this.scanner.read().getKind();

            atom = new BinaryExpression(
                    atom.getSourcePosition(),
                    this.toExpression(atom),
                    kind == Token.Kind.MUL ? BinaryExpression.Kind.MUL :
                    kind == Token.Kind.DIV ? BinaryExpression.Kind.DIV :
                    BinaryExpression.Kind.MOD,
                    this.toExpression(this.parseUnaryExpression())
            );
        }

        return atom;
    }

    /**
     * <pre>
     *   UnaryExpression:
     *     PreIncrementExpression
     *     PreDecrementExpression
     *     + UnaryExpression
     *     - UnaryExpression
     *     UnaryExpressionNotPlusMinus
     *
     *   PreIncrementExpression:
     *     ++ UnaryExpression
     *
     *   PreDecrementExpression:
     *     -- UnaryExpression
     *
     *   UnaryExpressionNotPlusMinus:
     *     PostfixExpression
     *     ~ UnaryExpression
     *     ! UnaryExpression
     *     CastExpression
     * </pre>
     */
    private Atom parseUnaryExpression()
        throws ParserException, IOException
    {
        throw this.error("TODO: parse unary expression");
    }

    private Type parseType()
        throws ParserException, IOException
    {
        if (this.scanner.peek().is(
                    Token.Kind.KEYWORD_BOOLEAN,
                    Token.Kind.KEYWORD_BYTE,
                    Token.Kind.KEYWORD_CHAR,
                    Token.Kind.KEYWORD_DOUBLE,
                    Token.Kind.KEYWORD_FLOAT,
                    Token.Kind.KEYWORD_INT,
                    Token.Kind.KEYWORD_LONG,
                    Token.Kind.KEYWORD_SHORT))
        {
            Type type = this.parsePrimitiveType();

            while (this.scanner.peek().is(Token.Kind.LBRACK)
                    && this.scanner.peekNextButOne().is(Token.Kind.RBRACK))
            {
                type = new ArrayType(type.getSourcePosition(), type);
                this.scanner.read();
                this.scanner.read();
            }

            return type;
        }

        return this.parseReferenceType();
    }

    private PrimitiveType parsePrimitiveType()
        throws ParserException, IOException
    {
        Token token = this.scanner.read();
        Primitive kind;

        switch (token.getKind())
        {
            case KEYWORD_BOOLEAN:
                kind = Primitive.BOOLEAN;
                break;

            case KEYWORD_BYTE:
                kind = Primitive.BYTE;
                break;

            case KEYWORD_CHAR:
                kind = Primitive.CHAR;
                break;

            case KEYWORD_DOUBLE:
                kind = Primitive.DOUBLE;
                break;

            case KEYWORD_FLOAT:
                kind = Primitive.FLOAT;
                break;

            case KEYWORD_INT:
                kind = Primitive.INT;
                break;

            case KEYWORD_LONG:
                kind = Primitive.LONG;
                break;

            case KEYWORD_SHORT:
                kind = Primitive.SHORT;
                break;

            default:
                throw this.error("unexpected %s; missing primitive type", token);
        }

        return new PrimitiveType(token.getSourcePosition(), kind);
    }

    private ReferenceType parseReferenceType()
        throws ParserException, IOException
    {
        throw this.error("TODO: parse reference type");
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

    private AssignableExpression toAssignableExpression(Atom atom)
    {
        if (atom instanceof AssignableExpression)
        {
            return (AssignableExpression) atom;
        }

        throw this.error("unexpected %s; missing variable", atom);
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
