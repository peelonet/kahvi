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
    private Token nextToken;
    private Token nextButOneToken;

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
        if (this.nextToken == null)
        {
            return this.scanner.getSourcePosition();
        } else {
            return this.nextToken.getSourcePosition();
        }
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

        if (this.peek(Token.Kind.KW_PACKAGE)
            && this.peekNextButOne(Token.Kind.IDENTIFIER))
        {
            packageDeclaration = this.parsePackageDeclaration();
        } else {
            packageDeclaration = null;
        }
        while (this.peek(Token.Kind.KW_IMPORT))
        {
            imports.add(this.parseImportDeclaration());
        }
        for (;;)
        {
            if (this.peek(Token.Kind.EOF))
            {
                return new CompilationUnit(
                        packageDeclaration,
                        imports,
                        typeDeclarations
                );
            }
            else if (!this.peekRead(Token.Kind.SEMICOLON))
            {
                typeDeclarations.add(
                        this.parseTypeDeclaration(this.parseModifiers())
                );
            }
        }
    }

    private PackageDeclaration parsePackageDeclaration()
        throws ParserException, IOException
    {
        SourcePosition position = this.getSourcePosition();
        Name packageName;

        this.expect(Token.Kind.KW_PACKAGE);
        packageName = this.parseQualifiedIdentifier();
        this.expect(Token.Kind.SEMICOLON);

        return new PackageDeclaration(
                position,
                Collections.<Annotation>emptyList(),
                packageName
        );
    }

    private ImportDeclaration parseImportDeclaration()
        throws ParserException, IOException
    {
        SourcePosition position = this.getSourcePosition();
        boolean _static;
        Name className;
        boolean onDemand;

        this.expect(Token.Kind.KW_IMPORT);
        if (this.peek(Token.Kind.KW_STATIC))
        {
            this.read();
            _static = true;
        } else {
            _static = false;
        }
        className = this.parseQualifiedIdentifier();
        if (this.peek(Token.Kind.DOT) && this.peekNextButOne(Token.Kind.MUL))
        {
            this.read();
            this.read();
            onDemand = true;
        } else {
            onDemand = false;
        }
        this.expect(Token.Kind.SEMICOLON);

        return new ImportDeclaration(
                position,
                _static,
                className,
                onDemand
        );
    }

    /**
     * <pre>
     *   TypeDeclaration:
     *     ClassDeclaration
     *     InterfaceDeclaration
     *     ;
     *
     *   ClassDeclaration:
     *     NormalClassDeclaration
     *     EnumDeclaration
     *
     *   InterfaceDeclaration:
     *     NormalInterfaceDeclaration
     *     AnnotationTypeDeclaration
     * </pre>
     */
    private TypeDeclaration parseTypeDeclaration(Modifiers modifiers)
        throws ParserException, IOException
    {
        if (this.peek(Token.Kind.KW_CLASS))
        {
            return this.parseClassDeclaration(modifiers);
        }
        else if (this.peek(Token.Kind.KW_INTERFACE))
        {
            throw this.error("TODO: parse interface declaration");
        }
        else if (this.peek(Token.Kind.KW_ENUM))
        {
            throw this.error("TODO: parse enum declaration");
        }
        else if (this.peek(Token.Kind.AT) && this.peekNextButOne(Token.Kind.KW_INTERFACE))
        {
            throw this.error("TODO: parse annotation declaration");
        }

        throw this.error("unexpected %s; missing type declaration", this.nextToken);
    }

    private ClassDeclaration parseClassDeclaration(Modifiers modifiers)
        throws ParserException, IOException
    {
        SourcePosition position = this.getSourcePosition();
        String simpleName;
        List<TypeParameterDeclaration> typeParameters;
        ClassType extendsClause;
        List<ClassType> implementsClause;
        List<TypeBodyDeclaration> members = new ArrayList<TypeBodyDeclaration>(3);

        this.expect(Token.Kind.KW_CLASS);
        simpleName = this.readIdentifier();
        if (this.peek(Token.Kind.LT))
        {
            typeParameters = this.parseTypeParameterList();
        } else {
            typeParameters = Collections.emptyList();
        }
        if (this.peekRead(Token.Kind.KW_EXTENDS))
        {
            extendsClause = this.parseClassType();
        } else {
            extendsClause = null;
        }
        if (this.peekRead(Token.Kind.KW_IMPLEMENTS))
        {
            implementsClause = this.parseClassTypeList();
        } else {
            implementsClause = Collections.emptyList();
        }
        this.expect(Token.Kind.LBRACE);
        for (;;)
        {
            if (this.peekRead(Token.Kind.RBRACE))
            {
                return new ClassDeclaration(
                        position,
                        modifiers,
                        simpleName,
                        typeParameters,
                        extendsClause,
                        implementsClause,
                        members
                );
            }
            else if (!this.peekRead(Token.Kind.SEMICOLON))
            {
                members.add(this.parseClassBodyDeclaration(simpleName));
            }
        }
    }

    private TypeBodyDeclaration parseClassBodyDeclaration(String className)
        throws ParserException, IOException
    {
        Modifiers modifiers = this.parseModifiers();
        
        if (this.peek(Token.Kind.LT))
        {
            List<TypeParameterDeclaration> typeParameters = this.parseTypeParameterList();

            if (className != null
                && this.peek(className)
                && this.peekNextButOne(Token.Kind.LPAREN))
            {
                throw this.error("TODO: parse constructor declaration");
            }
            else if (this.peek(Token.Kind.KW_VOID))
            {
                return this.parseMethodDeclaration(
                        modifiers,
                        typeParameters,
                        new VoidType(this.read().getSourcePosition()),
                        this.readIdentifier()
                );
            }
            else if (this.peek(Token.Kind.IDENTIFIER)
                    && this.peekNextButOne(Token.Kind.LPAREN))
            {
                return this.parseMethodDeclaration(
                        modifiers,
                        typeParameters,
                        null,
                        this.readIdentifier()
                );
            } else {
                return this.parseMethodDeclaration(
                        modifiers,
                        typeParameters,
                        this.parseType(),
                        this.readIdentifier()
                );
            }
        }
        else if (this.peek(Token.Kind.KW_CLASS))
        {
            return this.parseClassDeclaration(modifiers);
        }
        else if (this.peek(Token.Kind.KW_INTERFACE))
        {
            throw this.error("TODO: member interface declaration");
        }
        else if (this.peek(Token.Kind.KW_ENUM))
        {
            throw this.error("TODO: member enum declaration");
        }
        else if (this.peek(Token.Kind.LBRACE))
        {
            throw this.error("TODO: initializer declaration");
        }
        else if (className != null
                && this.peek(className)
                && this.peekNextButOne(Token.Kind.LPAREN))
        {
            throw this.error("TODO: constructor declaration");
        }
        else if (this.peek(Token.Kind.KW_VOID))
        {
            return this.parseMethodDeclaration(
                    modifiers,
                    Collections.<TypeParameterDeclaration>emptyList(),
                    new VoidType(this.read().getSourcePosition()),
                    this.readIdentifier()
            );
        }
        else if (this.peek(Token.Kind.IDENTIFIER)
                && this.peekNextButOne(Token.Kind.LPAREN))
        {
            return this.parseMethodDeclaration(
                    modifiers,
                    Collections.<TypeParameterDeclaration>emptyList(),
                    null,
                    this.readIdentifier()
            );
        } else {
            Type type = this.parseType();
            String name = this.readIdentifier();

            if (this.peek(Token.Kind.LPAREN))
            {
                return this.parseMethodDeclaration(
                        modifiers,
                        Collections.<TypeParameterDeclaration>emptyList(),
                        type,
                        name
                );
            } else {
                return this.parseFieldDeclaration(modifiers, type, name);
            }
        }
    }

    private FieldDeclaration parseFieldDeclaration(Modifiers modifiers,
                                                   Type type,
                                                   String name)
        throws ParserException, IOException
    {
        VariableInitializer initializer;
        ReadMethodDeclaration readMethod = null;
        List<WriteMethodDeclaration> writeMethods = null;

        if (this.peekRead(Token.Kind.ASSIGN))
        {
            initializer = this.parseVariableInitializer();
        } else {
            initializer = null;
        }
        if (this.peekRead(Token.Kind.LBRACE))
        {
            while (!this.peekRead(Token.Kind.RBRACE))
            {
                Modifiers modifiers2 = this.parseModifiers();

                if (this.peek("get"))
                {
                    ReadMethodDeclaration rmd = this.parseReadMethodDeclaration(modifiers2);

                    if (readMethod != null)
                    {
                        throw this.error("field has multiple read methods");
                    }
                    readMethod = rmd;
                }
                else if (this.peek("set"))
                {
                    WriteMethodDeclaration wmd = this.parseWriteMethodDeclaration(modifiers2);

                    if (writeMethods == null)
                    {
                        writeMethods = new ArrayList<WriteMethodDeclaration>(3);
                    }
                } else {
                    throw this.error(
                            "unexpected %s; missing property declaration",
                            this.peek()
                    );
                }
            }
        } else {
            this.expect(Token.Kind.SEMICOLON);
        }
        if (writeMethods == null)
        {
            writeMethods = Collections.emptyList();
        }

        return new FieldDeclaration(
                type.getSourcePosition(),
                modifiers,
                type,
                name,
                initializer,
                readMethod,
                writeMethods
        );
    }

    private MethodDeclaration parseMethodDeclaration(
            Modifiers modifiers,
            List<TypeParameterDeclaration> typeParameters,
            Type type,
            String name)
        throws ParserException, IOException
    {
        SourcePosition position = this.getSourcePosition();
        List<ParameterDeclaration> parameters = this.parseParameterList();
        List<ClassType> _throws;
        List<Expression> preConditions;
        BlockStatement body;

        if (this.peekRead(Token.Kind.KW_THROWS))
        {
            _throws = this.parseClassTypeList();
        } else {
            _throws = Collections.emptyList();
        }
        preConditions = this.parsePreConditionList();
        if (this.peekRead(Token.Kind.SEMICOLON))
        {
            if (!modifiers.isAbstract() && !modifiers.isNative())
            {
                throw this.error(
                        "non-abstract, non-native methods must have a body"
                );
            }
            body = null;
        }
        else if (modifiers.isAbstract() || modifiers.isNative())
        {
            throw this.error(
                    "abstract or native methods must not have a body"
            );
        } else {
            body = this.parseMethodBody();
        }

        return new MethodDeclaration(
                position,
                modifiers,
                typeParameters,
                type,
                name,
                parameters,
                _throws,
                preConditions,
                body
        );
    }

    private ReadMethodDeclaration parseReadMethodDeclaration(Modifiers modifiers)
        throws ParserException, IOException
    {
        SourcePosition position = this.getSourcePosition();
        List<ClassType> _throws;
        List<Expression> preConditions;
        BlockStatement block;

        this.expect("get");
        if (this.peekRead(Token.Kind.LPAREN))
        {
            this.expect(Token.Kind.RPAREN);
            if (this.peekRead(Token.Kind.KW_THROWS))
            {
                _throws = this.parseClassTypeList();
            } else {
                _throws = Collections.emptyList();
            }
            preConditions = this.parsePreConditionList();
            block = this.parseMethodBody();
        } else {
            _throws = Collections.emptyList();
            preConditions = Collections.emptyList();
            block = null;
            this.expect(Token.Kind.SEMICOLON);
        }

        return new ReadMethodDeclaration(
                position,
                modifiers,
                _throws,
                preConditions,
                block
        );
    }

    private WriteMethodDeclaration parseWriteMethodDeclaration(Modifiers modifiers)
        throws ParserException, IOException
    {
        SourcePosition position = this.getSourcePosition();
        List<ParameterDeclaration> parameters;
        List<ClassType> _throws;
        List<Expression> preConditions;
        BlockStatement block;

        this.expect("set");
        if (this.peek(Token.Kind.LPAREN))
        {
            parameters = this.parseParameterList();
            if (this.peekRead(Token.Kind.KW_THROWS))
            {
                _throws = this.parseClassTypeList();
            } else {
                _throws = Collections.emptyList();
            }
            preConditions = this.parsePreConditionList();
            block = this.parseMethodBody();
        } else {
            parameters = Collections.emptyList();
            _throws = Collections.emptyList();
            preConditions = Collections.emptyList();
            block = null;
            this.expect(Token.Kind.SEMICOLON);
        }

        return new WriteMethodDeclaration(
                position,
                modifiers,
                parameters,
                _throws,
                preConditions,
                block
        );
    }

    private List<Expression> parsePreConditionList()
        throws ParserException, IOException
    {
        List<Expression> list = null;

        while (this.peekRead("requires"))
        {
            Expression expression = this.toExpression(this.parseExpression());

            if (list == null)
            {
                list = new ArrayList<Expression>(3);
            }
            list.add(expression);
        }
        if (list == null)
        {
            return Collections.emptyList();
        } else {
            return list;
        }
    }

    private ParameterDeclaration parseParameter()
        throws ParserException, IOException
    {
        SourcePosition position = this.getSourcePosition();
        List<Annotation> annotations = this.parseAnnotationList();
        boolean _final;
        Type type;
        boolean variadic;
        boolean nullable;
        String name;

        _final = this.peekRead(Token.Kind.KW_FINAL);
        type = this.parseType();
        variadic = this.peekRead(Token.Kind.DOT_DOT_DOT);
        nullable = !this.peekRead(Token.Kind.NOT);
        name = this.readIdentifier();

        return new ParameterDeclaration(
                position,
                annotations,
                _final,
                type,
                variadic,
                nullable,
                name
        );
    }

    private List<ParameterDeclaration> parseParameterList()
        throws ParserException, IOException
    {
        List<ParameterDeclaration> list = null;

        this.expect(Token.Kind.LPAREN);
        while (!this.peek(Token.Kind.RPAREN))
        {
            ParameterDeclaration parameter = this.parseParameter();

            if (list == null)
            {
                list = new ArrayList<ParameterDeclaration>(3);
            }
            list.add(parameter);
            if (parameter.isVariadic() || !this.peekRead(Token.Kind.COMMA))
            {
                break;
            }
        }
        this.expect(Token.Kind.RPAREN);
        if (list == null)
        {
            return Collections.emptyList();
        } else {
            return list;
        }
    }

    /**
     * <pre>
     *   MethodBody:
     *     =&gt; Expression ;
     *     =&gt; ThrowStatement
     *     Block
     * </pre>
     */
    private BlockStatement parseMethodBody()
        throws ParserException, IOException
    {
        if (this.peekRead(Token.Kind.ARROW))
        {
            SourcePosition position = this.getSourcePosition();
            List<Statement> list = new ArrayList<Statement>(1);

            if (this.peek(Token.Kind.KW_THROW))
            {
                list.add(this.parseThrowStatement());
            } else {
                list.add(new ReturnStatement(
                            position,
                            this.toExpression(this.parseExpression())
                ));
                this.expect(Token.Kind.SEMICOLON);
            }

            return new BlockStatement(position, list);
        }

        return this.parseBlock();
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
        SourcePosition position = this.getSourcePosition();
        Name name;

        this.expect(Token.Kind.AT);
        name = this.parseQualifiedIdentifier();
        if (this.peekRead(Token.Kind.LPAREN))
        {
            if (this.peek(Token.Kind.IDENTIFIER)
                && this.peekNextButOne(Token.Kind.ASSIGN))
            {
                throw this.error("TODO: parse normal annotation");
            } else {
                throw this.error("TODO: parse element annotation");
            }
        }

        return new MarkerAnnotation(new ClassType(position, name));
    }

    private List<Annotation> parseAnnotationList()
        throws ParserException, IOException
    {
        List<Annotation> list = null;

        while (this.peek(Token.Kind.AT)
                && this.peekNextButOne(Token.Kind.IDENTIFIER))
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
     *   ElementValue:
     *     ConditionalExpression
     *     ElementValueArrayInitializer
     *     Annotation
     * </pre>
     */
    private ElementValue parseElementValue()
        throws ParserException, IOException
    {
        if (this.peek(Token.Kind.LBRACE))
        {
            return this.parseElementValueArrayInitializer();
        }
        else if (this.peek(Token.Kind.AT))
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

        this.expect(Token.Kind.LBRACE);
        while (!this.peek(Token.Kind.RBRACE))
        {
            ElementValue value = this.parseElementValue();

            if (list == null)
            {
                list = new ArrayList<ElementValue>(3);
            }
            list.add(value);
            if (this.peek(Token.Kind.COMMA))
            {
                this.read();
            } else {
                break;
            }
        }
        this.expect(Token.Kind.RBRACE);
        if (list == null)
        {
            list = Collections.emptyList();
        }

        return new ElementValueArrayInitializer(list);
    }

    private Name parseQualifiedIdentifier()
        throws ParserException, IOException
    {
        Name result = new Name(this.readIdentifier());

        while (this.peek(Token.Kind.DOT)
                && this.peekNextButOne(Token.Kind.IDENTIFIER))
        {
            this.read();
            result = new Name(result, this.read().getText());
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
            if (this.peekRead(Token.Kind.KW_PUBLIC))
            {
                if (visibility != null)
                {
                    throw this.error("visibility is both '%s' and 'public'", visibility);
                }
                visibility = Visibility.PUBLIC;
            }
            else if (this.peekRead(Token.Kind.KW_PROTECTED))
            {
                if (visibility != null)
                {
                    throw this.error("visibility is both '%s' and 'protected'", visibility);
                }
                visibility = Visibility.PROTECTED;
            }
            else if (this.peekRead(Token.Kind.KW_PRIVATE))
            {
                if (visibility != null)
                {
                    throw this.error("visibility is both '%s' and 'private'", visibility);
                }
                visibility = Visibility.PRIVATE;
            }
            else if (this.peekRead(Token.Kind.KW_PACKAGE))
            {
                if (visibility != null)
                {
                    throw this.error("visibility is both '%s' and 'package'", visibility);
                }
                visibility = Visibility.PACKAGE;
            } else {
                Flag flag;

                if (this.peekRead(Token.Kind.KW_STATIC))
                {
                    flag = Flag.STATIC;
                }
                else if (this.peekRead(Token.Kind.KW_ABSTRACT))
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
                else if (this.peekRead(Token.Kind.KW_FINAL))
                {
                    if (flags.contains(Flag.ABSTRACT))
                    {
                        throw this.error("'final' are 'abstract' are mutually exclusive");
                    }
                    flag = Flag.FINAL;
                }
                else if (this.peekRead(Token.Kind.KW_NATIVE))
                {
                    if (flags.contains(Flag.ABSTRACT))
                    {
                        throw this.error("'native' and 'abstract' are mutually exclusive");
                    }
                    flag = Flag.NATIVE;
                }
                else if (this.peekRead(Token.Kind.KW_SYNCHRONIZED))
                {
                    flag = Flag.SYNCHRONIZED;
                }
                else if (this.peekRead(Token.Kind.KW_TRANSIENT))
                {
                    flag = Flag.TRANSIENT;
                }
                else if (this.peekRead(Token.Kind.KW_VOLATILE))
                {
                    flag = Flag.VOLATILE;
                }
                else if (this.peekRead(Token.Kind.KW_STRICTFP))
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

    private Statement parseBlockStatement()
        throws ParserException, IOException
    {
        if ((this.peek(Token.Kind.IDENTIFIER)
            && this.peekNextButOne(Token.Kind.COLON))
            || (this.peek(Token.Kind.KW_IF,
                          Token.Kind.KW_FOR,
                          Token.Kind.KW_WHILE,
                          Token.Kind.KW_DO,
                          Token.Kind.KW_TRY,
                          Token.Kind.KW_SWITCH,
                          Token.Kind.KW_SYNCHRONIZED,
                          Token.Kind.KW_RETURN,
                          Token.Kind.KW_THROW,
                          Token.Kind.KW_BREAK,
                          Token.Kind.KW_CONTINUE,
                          Token.Kind.KW_ASSERT,
                          Token.Kind.LBRACE,
                          Token.Kind.SEMICOLON)))
        {
            return this.parseStatement();
        }
        else if (this.peek(Token.Kind.KW_CLASS))
        {
            throw this.error("TODO: local class declaration");
        }
        else if (this.peekRead(Token.Kind.KW_FINAL))
        {
            SourcePosition position = this.getSourcePosition();
            Type type;
            boolean nullable;
            String name;
            VariableInitializer initializer;
            VariableStatement statement;

            if (this.peekRead("var"))
            {
                type = null;
            } else {
                type = this.parseType();
            }
            nullable = !this.peekRead(Token.Kind.NOT);
            name = this.readIdentifier();
            if (this.peekRead(Token.Kind.ASSIGN))
            {
                initializer = this.parseVariableInitializer();
            } else {
                initializer = null;
            }
            statement = new VariableStatement(
                    position,
                    true,
                    type,
                    nullable,
                    name,
                    initializer
            );
            this.expect(Token.Kind.SEMICOLON);

            return statement;
        }
        else if (this.peekRead("var"))
        {
            SourcePosition position = this.getSourcePosition();
            boolean nullable;
            String name;
            VariableInitializer initializer;
            VariableStatement statement;

            nullable = !this.peekRead(Token.Kind.NOT);
            name = this.readIdentifier();
            if (this.peekRead(Token.Kind.ASSIGN))
            {
                initializer = this.parseVariableInitializer();
            } else {
                initializer = null;
            }
            statement = new VariableStatement(
                    position,
                    false,
                    null,
                    nullable,
                    name,
                    initializer
            );
            this.expect(Token.Kind.SEMICOLON);

            return statement;
        } else {
            Atom atom = this.parseExpression();

            if (this.peekRead(Token.Kind.SEMICOLON))
            {
                return new ExpressionStatement(
                        atom.getSourcePosition(),
                        this.toExpression(atom)
                );
            }

            throw this.error("TODO: local variable declaration");
        }
    }

    private Statement parseStatement()
        throws ParserException, IOException
    {
        if (this.peek(Token.Kind.IDENTIFIER)
            && this.peekNextButOne(Token.Kind.COLON))
        {
            return this.parseLabeledStatement();
        }
        else if (this.peek(Token.Kind.LBRACE))
        {
            return this.parseBlock();
        }
        else if (this.peek(Token.Kind.KW_IF))
        {
            return this.parseIfStatement();
        }
        else if (this.peek(Token.Kind.KW_FOR))
        {
            return this.parseForStatement();
        }
        else if (this.peek(Token.Kind.KW_WHILE))
        {
            return this.parseWhileStatement();
        }
        else if (this.peek(Token.Kind.KW_DO))
        {
            return this.parseDoWhileStatement();
        }
        else if (this.peek(Token.Kind.KW_TRY))
        {
            return this.parseTryStatement();
        }
        else if (this.peek(Token.Kind.KW_SWITCH))
        {
            return this.parseSwitchStatement();
        }
        else if (this.peek(Token.Kind.KW_SYNCHRONIZED))
        {
            return this.parseSynchronizedStatement();
        }
        else if (this.peek(Token.Kind.KW_RETURN))
        {
            return this.parseReturnStatement();
        }
        else if (this.peek(Token.Kind.KW_THROW))
        {
            return this.parseThrowStatement();
        }
        else if (this.peek(Token.Kind.KW_BREAK))
        {
            return this.parseBreakStatement();
        }
        else if (this.peek(Token.Kind.KW_CONTINUE))
        {
            return this.parseContinueStatement();
        }
        else if (this.peek(Token.Kind.KW_ASSERT))
        {
            return this.parseAssertStatement();
        }
        else if (this.peek(Token.Kind.SEMICOLON))
        {
            return this.parseEmptyStatement();
        } else {
            return this.parseExpressionStatement();
        }
    }

    /**
     * <pre>
     *   Block:
     *     { {Statement} }
     * </pre>
     */
    private BlockStatement parseBlock()
        throws ParserException, IOException
    {
        SourcePosition position = this.getSourcePosition();
        List<Statement> list = null;

        this.expect(Token.Kind.LBRACE);
        while (!this.peekRead(Token.Kind.RBRACE))
        {
            Statement statement = this.parseBlockStatement();

            if (list == null)
            {
                list = new ArrayList<Statement>(3);
            }
            list.add(statement);
        }
        if (list == null)
        {
            list = Collections.emptyList();
        }

        return new BlockStatement(position, list);
    }

    /**
     * <pre>
     *   LabeledStatement:
     *     Identifier : Statement
     * </pre>
     */
    private LabeledStatement parseLabeledStatement()
        throws ParserException, IOException
    {
        SourcePosition position = this.getSourcePosition();
        String label = this.readIdentifier();

        this.expect(Token.Kind.COLON);

        return new LabeledStatement(
                position,
                new Name(label),
                this.parseStatement()
        );
    }

    /**
     * <pre>
     *   IfStatement:
     *     if ( Expression ) Statement [else Statement]
     * </pre>
     */
    private IfStatement parseIfStatement()
        throws ParserException, IOException
    {
        SourcePosition position = this.getSourcePosition();
        Expression condition;
        Statement thenStatement;
        Statement elseStatement;

        this.expect(Token.Kind.KW_IF);
        this.expect(Token.Kind.LPAREN);
        condition = this.toExpression(this.parseExpression());
        this.expect(Token.Kind.RPAREN);
        thenStatement = this.parseStatement();
        if (this.peekRead(Token.Kind.KW_ELSE))
        {
            elseStatement = this.parseStatement();
        } else {
            elseStatement = null;
        }

        return new IfStatement(
                position,
                condition,
                thenStatement,
                elseStatement
        );
    }

    private Statement parseForStatement()
        throws ParserException, IOException
    {
        throw this.error("TODO: parse for statement");
    }

    /**
     * <pre>
     *   WhileStatement:
     *     while ( Expression ) Statement
     * </pre>
     */
    private WhileStatement parseWhileStatement()
        throws ParserException, IOException
    {
        SourcePosition position = this.getSourcePosition();
        Expression condition;
        Statement statement;

        this.expect(Token.Kind.KW_WHILE);
        this.expect(Token.Kind.LPAREN);
        condition = this.toExpression(this.parseExpression());
        this.expect(Token.Kind.RPAREN);
        statement = this.parseStatement();

        return new WhileStatement(position, condition, statement);
    }

    /**
     * <pre>
     *   DoWhileStatement:
     *     do Statement while ( Expression ) ;
     * </pre>
     */
    private DoWhileStatement parseDoWhileStatement()
        throws ParserException, IOException
    {
        SourcePosition position = this.getSourcePosition();
        Statement statement;
        Expression condition;

        this.expect(Token.Kind.KW_DO);
        statement = this.parseStatement();
        this.expect(Token.Kind.KW_WHILE);
        this.expect(Token.Kind.LPAREN);
        condition = this.toExpression(this.parseExpression());
        this.expect(Token.Kind.RPAREN);
        this.expect(Token.Kind.SEMICOLON);

        return new DoWhileStatement(position, statement, condition);
    }

    private Statement parseTryStatement()
        throws ParserException, IOException
    {
        throw this.error("TODO: parse try statement");
    }

    private SwitchStatement parseSwitchStatement()
        throws ParserException, IOException
    {
        SourcePosition position = this.getSourcePosition();
        Expression expression;
        List<SwitchStatement.Case> cases = new ArrayList<SwitchStatement.Case>(3);
        boolean hasDefault = false;

        this.expect(Token.Kind.KW_SWITCH);
        this.expect(Token.Kind.LPAREN);
        expression = this.toExpression(this.parseExpression());
        this.expect(Token.Kind.RPAREN);
        this.expect(Token.Kind.LBRACE);
        while (!this.peekRead(Token.Kind.RBRACE))
        {
            SourcePosition position2 = this.getSourcePosition();
            Expression condition;
            List<Statement> statements;

            if (this.peekRead(Token.Kind.KW_CASE))
            {
                condition = this.toExpression(this.parseExpression());
            }
            else if (this.peekRead(Token.Kind.KW_DEFAULT))
            {
                if (hasDefault)
                {
                    throw this.error("duplicate `default' label");
                }
                hasDefault = true;
                condition = null;
            } else {
                throw this.error("`case' or `default' expected");
            }
            this.expect(Token.Kind.COLON);
            statements = new ArrayList<Statement>(3);
            while (!this.peek(Token.Kind.KW_CASE,
                              Token.Kind.KW_DEFAULT,
                              Token.Kind.RBRACE))
            {
                statements.add(this.parseBlockStatement());
            }
            if (statements.isEmpty())
            {
                throw this.error("empty case");
            }
            cases.add(new SwitchStatement.Case(position2, condition, statements));
        }
        if (cases.isEmpty())
        {
            throw this.error("no cases in `switch' statement");
        }

        return new SwitchStatement(position, expression, cases);
    }

    /**
     * <pre>
     *   SynchronizedStatement:
     *     synchronized ( Expression ) Block
     * </pre>
     */
    private SynchronizedStatement parseSynchronizedStatement()
        throws ParserException, IOException
    {
        SourcePosition position = this.getSourcePosition();
        Expression expression;
        BlockStatement block;

        this.expect(Token.Kind.KW_SYNCHRONIZED);
        this.expect(Token.Kind.LPAREN);
        expression = this.toExpression(this.parseExpression());
        this.expect(Token.Kind.RPAREN);
        block = this.parseBlock();

        return new SynchronizedStatement(position, expression, block);
    }

    /**
     * <pre>
     *   ReturnStatement:
     *     return [Expression] ;
     * </pre>
     */
    private ReturnStatement parseReturnStatement()
        throws ParserException, IOException
    {
        SourcePosition position = this.getSourcePosition();
        Expression expression;

        this.expect(Token.Kind.KW_RETURN);
        if (this.peek(Token.Kind.SEMICOLON))
        {
            expression = null;
        } else {
            expression = this.toExpression(this.parseExpression());
        }
        this.expect(Token.Kind.SEMICOLON);

        return new ReturnStatement(position, expression);
    }

    /**
     * <pre>
     *   ThrowStatement:
     *     throw Expression ;
     * </pre>
     */
    private ThrowStatement parseThrowStatement()
        throws ParserException, IOException
    {
        SourcePosition position = this.getSourcePosition();
        Expression expression;

        this.expect(Token.Kind.KW_THROW);
        expression = this.toExpression(this.parseExpression());
        this.expect(Token.Kind.SEMICOLON);

        return new ThrowStatement(position, expression);
    }

    /**
     * <pre>
     *   BreakStatement:
     *     break [Identifier] ;
     * </pre>
     */
    private BreakStatement parseBreakStatement()
        throws ParserException, IOException
    {
        SourcePosition position = this.getSourcePosition();
        Name label;

        this.expect(Token.Kind.KW_BREAK);
        if (this.peek(Token.Kind.SEMICOLON))
        {
            label = null;
        } else {
            label = new Name(this.readIdentifier());
        }
        this.expect(Token.Kind.SEMICOLON);

        return new BreakStatement(position, label);
    }

    /**
     * <pre>
     *   ContinueStatement:
     *     continue [Identifier] ;
     * </pre>
     */
    private ContinueStatement parseContinueStatement()
        throws ParserException, IOException
    {
        SourcePosition position = this.getSourcePosition();
        Name label;

        this.expect(Token.Kind.KW_CONTINUE);
        if (this.peek(Token.Kind.SEMICOLON))
        {
            label = null;
        } else {
            label = new Name(this.readIdentifier());
        }
        this.expect(Token.Kind.SEMICOLON);

        return new ContinueStatement(position, label);
    }

    /**
     * <pre>
     *   AssertStatement:
     *     assert Expression [: Expression] ;
     * </pre>
     */
    private AssertStatement parseAssertStatement()
        throws ParserException, IOException
    {
        SourcePosition position = this.getSourcePosition();
        Expression condition;
        Expression detail;

        this.expect(Token.Kind.KW_ASSERT);
        condition = this.toExpression(this.parseExpression());
        if (this.peekRead(Token.Kind.COLON))
        {
            detail = this.toExpression(this.parseExpression());
        } else {
            detail = null;
        }
        this.expect(Token.Kind.SEMICOLON);

        return new AssertStatement(position, condition, detail);
    }

    /**
     * <pre>
     *   EmptyStatement:
     *     ;
     * </pre>
     */
    private EmptyStatement parseEmptyStatement()
        throws ParserException, IOException
    {
        SourcePosition position = this.getSourcePosition();
        
        this.expect(Token.Kind.SEMICOLON);

        return new EmptyStatement(position);
    }

    /**
     * <pre>
     *   ExpressionStatement:
     *     Expression ;
     * </pre>
     */
    private ExpressionStatement parseExpressionStatement()
        throws ParserException, IOException
    {
        Expression expression = this.toExpression(this.parseExpression());

        this.expect(Token.Kind.SEMICOLON);

        return new ExpressionStatement(
                expression.getSourcePosition(),
                expression
        );
    }

    /**
     * <pre>
     *   VariableInitializer:
     *     ArrayInitializer
     *     Expression
     * </pre>
     */
    private VariableInitializer parseVariableInitializer()
        throws ParserException, IOException
    {
        if (this.peek(Token.Kind.LBRACE))
        {
            throw this.error("TODO: parse array initializer");
        } else {
            return this.toExpression(this.parseExpression());
        }
    }

    /**
     * <pre>
     *   Expression:
     *     AssignmentExpression
     * </pre>
     */
    private Atom parseExpression()
        throws ParserException, IOException
    {
        return this.parseAssignmentExpression();
    }

    /**
     * <pre>
     *   AssignmentExpression:
     *     ConditionalExpression [AssignmentOperator Expression]
     *
     *   AssignmentOperator:
     *     =
     *     +=
     *     -=
     *     /=
     *     %=
     *     &lt;&lt;=
     *     &gt;&gt;=
     *     &gt;&gt;&gt;=
     *     &amp;=
     *     |=
     *     ^=
     * </pre>
     */
    private Atom parseAssignmentExpression()
        throws ParserException, IOException
    {
        Atom atom = this.parseConditionalExpression();

        if (this.peekRead(Token.Kind.ASSIGN))
        {
            return new AssignmentExpression(
                    atom.getSourcePosition(),
                    this.toAssignableExpression(atom),
                    this.toExpression(this.parseExpression())
            );
        }
        else if (this.peek(Token.Kind.ASSIGN_ADD,
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
            Token.Kind kind = this.read().getKind();

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

    /**
     * <pre>
     *   ConditionalExpression:
     *     ConditionalOrExpression [? Expression : ConditionalExpression]
     * </pre>
     */
    private Atom parseConditionalExpression()
        throws ParserException, IOException
    {
        Atom atom = this.parseConditionalOrExpression();

        if (this.peekRead(Token.Kind.CONDITIONAL))
        {
            Expression condition = this.toExpression(atom);
            Expression trueExpression = this.toExpression(this.parseExpression());
            Expression falseExpression;

            this.expect(Token.Kind.COLON);
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

        while (this.peekRead(Token.Kind.OR))
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

        while (this.peekRead(Token.Kind.AND))
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

        while (this.peekRead(Token.Kind.BIT_OR))
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

        while (this.peekRead(Token.Kind.BIT_XOR))
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

        while (this.peekRead(Token.Kind.BIT_AND))
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

        while (this.peek(Token.Kind.EQ, Token.Kind.NE))
        {
            Token.Kind kind = this.read().getKind();

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
            if (this.peek(Token.Kind.LT,
                          Token.Kind.GT,
                          Token.Kind.LTE,
                          Token.Kind.GTE))
            {
                Token.Kind kind = this.read().getKind();

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
            else if (this.peekRead(Token.Kind.KW_INSTANCEOF))
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

        while (this.peek(Token.Kind.LSH,
                         Token.Kind.RSH,
                         Token.Kind.RSH2))
        {
            Token.Kind kind = this.read().getKind();

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

        while (this.peek(Token.Kind.ADD, Token.Kind.SUB))
        {
            Token.Kind kind = this.read().getKind();

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

        while (this.peek(Token.Kind.MUL,
                         Token.Kind.DIV,
                         Token.Kind.MOD))
        {
            Token.Kind kind = this.read().getKind();

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
        if (this.peek(Token.Kind.INC, Token.Kind.DEC))
        {
            throw this.error("TODO: parse pre-increment/decrement");
        }
        else if (this.peek(Token.Kind.ADD,
                           Token.Kind.SUB,
                           Token.Kind.NOT,
                           Token.Kind.BIT_NOT))
        {
            throw this.error("TODO: parse unary operators");
        } else {
            Atom atom = this.parsePrimary();

            while (this.peek(Token.Kind.DOT, Token.Kind.LBRACK))
            {
                atom = this.parsePostfixExpression(atom);
            }
            while (this.peek(Token.Kind.INC, Token.Kind.DEC))
            {
                throw this.error("TODO: parse post-increment/decrement");
            }

            return atom;
        }
    }

    private Atom parsePrimary()
        throws ParserException, IOException
    {
        Token token = this.read();

        switch (token.getKind())
        {
            case LPAREN:
            {
                Atom atom = this.parseExpression();

                this.expect(Token.Kind.RPAREN);
                if (this.peek(Token.Kind.IDENTIFIER,
                              Token.Kind.LPAREN,
                              Token.Kind.BIT_NOT,
                              Token.Kind.NOT,
                              Token.Kind.KW_THIS,
                              Token.Kind.KW_SUPER,
                              Token.Kind.KW_NEW)) // TODO: literals also
                {
                    throw this.error("TODO: parse type cast expression");
                }

                return new ParenthesizedExpression(
                        atom.getSourcePosition(),
                        this.toExpression(atom)
                );
            }

            case IDENTIFIER:
                if (this.peek(Token.Kind.LPAREN))
                {
                    return new MethodInvocationExpression(
                            token.getSourcePosition(),
                            null,
                            Collections.<TypeArgument>emptyList(),
                            token.getText(),
                            this.parseArguments()
                    );
                } else {
                    return new IdentifierExpression(
                            token.getSourcePosition(),
                            token.getText()
                    );
                }

            case KW_THIS:
                throw this.error("TODO: parse 'this'");

            case KW_SUPER:
                throw this.error("TODO: parse 'super'");

            case KW_NEW:
                throw this.error("TODO: parse 'new'");

            case KW_TRUE:
            case KW_FALSE:
                return new BooleanLiteralExpression(
                        token.getSourcePosition(),
                        token.getKind() == Token.Kind.KW_TRUE
                );

            case KW_NULL:
                return new NullLiteralExpression(token.getSourcePosition());

            case LBRACK:
                throw this.error("TODO: parse list literal");

            case LBRACE:
                throw this.error("TODO: parse map or set literal");

            case AT:
                return new PropertyExpression(
                        token.getSourcePosition(),
                        this.readIdentifier()
                );

            case KW_BOOLEAN:
            case KW_BYTE:
            case KW_CHAR:
            case KW_DOUBLE:
            case KW_FLOAT:
            case KW_INT:
            case KW_LONG:
            case KW_SHORT:
                return this.parseType();

            case KW_VOID:
                if (this.peek(Token.Kind.DOT)
                    && this.peekNextButOne(Token.Kind.KW_CLASS))
                {
                    throw this.error("TODO: parse class literal");
                } else {
                    throw this.error("`void' encountered in wrong context");
                }

            case LT:
            {
                List<TypeArgument> typeArguments = this.parseTypeArgumentList();

                if (this.peek(Token.Kind.LBRACK)
                    && typeArguments.size() == 1
                    && typeArguments.get(0) instanceof ClassType)
                {
                    throw this.error("TODO: parse list literal");
                }
                else if (this.peek(Token.Kind.LBRACE))
                {
                    if (typeArguments.size() == 2
                        && typeArguments.get(0) instanceof ClassType
                        && typeArguments.get(1) instanceof ClassType)
                    {
                        throw this.error("TODO: parse map literal");
                    }
                    else if (typeArguments.size() == 1
                            && typeArguments.get(0) instanceof ClassType)
                    {
                        throw this.error("TODO: parse set literal");
                    }
                }

                return new MethodInvocationExpression(
                        token.getSourcePosition(),
                        null,
                        typeArguments,
                        this.readIdentifier(),
                        this.parseArguments()
                );
            }

            default:
                throw this.error(
                        token,
                        "unexpected %s; missing expression",
                        token
                );
        }
    }

    private Atom parsePostfixExpression(Atom atom)
        throws ParserException, IOException
    {
        if (this.peekRead(Token.Kind.DOT))
        {
            if (this.peek(Token.Kind.IDENTIFIER))
            {
                String identifier = this.readIdentifier();

                if (this.peek(Token.Kind.LPAREN))
                {
                    return new MethodInvocationExpression(
                            atom.getSourcePosition(),
                            this.toExpression(atom),
                            Collections.<TypeArgument>emptyList(),
                            identifier,
                            this.parseArguments()
                    );
                } else {
                    return new MemberSelectExpression(
                            atom.getSourcePosition(),
                            this.toExpression(atom),
                            identifier
                    );
                }
            }
            else if (this.peek(Token.Kind.LT))
            {
                return new MethodInvocationExpression(
                        atom.getSourcePosition(),
                        this.toExpression(atom),
                        this.parseTypeArgumentList(),
                        this.readIdentifier(),
                        this.parseArguments()
                );
            }
            else if (this.peekRead(Token.Kind.KW_THIS))
            {
                throw this.error("TODO: parse qualified 'this' expression");
            }
            else if (this.peekRead(Token.Kind.KW_SUPER))
            {
                throw this.error("TODO: parse super constructor invocation");
            }
            else if (this.peekRead(Token.Kind.KW_NEW))
            {
                throw this.error("TODO: parse qualified 'new' expression");
            }
            else if (this.peekRead(Token.Kind.KW_CLASS))
            {
                throw this.error("TODO: parse class literal");
            }
            else if (this.peekRead(Token.Kind.AT))
            {
                return new PropertyExpression(
                        atom.getSourcePosition(),
                        this.toExpression(atom),
                        this.readIdentifier()
                );
            }
        }
        else if (this.peekRead(Token.Kind.LBRACK))
        {
            Expression index = this.toExpression(this.parseExpression());

            this.expect(Token.Kind.RBRACK);

            return new ArrayAccessExpression(
                    atom.getSourcePosition(),
                    this.toExpression(atom),
                    index
            );
        }

        throw this.error("unexpected %s; missing selector", this.peek());
    }

    private Type parseType()
        throws ParserException, IOException
    {
        Type type;

        if (this.peek(Token.Kind.KW_BOOLEAN,
                      Token.Kind.KW_BYTE,
                      Token.Kind.KW_CHAR,
                      Token.Kind.KW_DOUBLE,
                      Token.Kind.KW_FLOAT,
                      Token.Kind.KW_INT,
                      Token.Kind.KW_LONG,
                      Token.Kind.KW_SHORT))
        {
            type = this.parsePrimitiveType();
        } else {
            type = this.parseClassType();
        }
        while (this.peek(Token.Kind.LBRACK)
                && this.peekNextButOne(Token.Kind.RBRACK))
        {
            this.read();
            this.read();
            type = new ArrayType(type.getSourcePosition(), type);
        }

        return type;
    }

    private PrimitiveType parsePrimitiveType()
        throws ParserException, IOException
    {
        Token token = this.read();
        Primitive kind;

        switch (token.getKind())
        {
            case KW_BOOLEAN:
                kind = Primitive.BOOLEAN;
                break;

            case KW_BYTE:
                kind = Primitive.BYTE;
                break;

            case KW_CHAR:
                kind = Primitive.CHAR;
                break;

            case KW_DOUBLE:
                kind = Primitive.DOUBLE;
                break;

            case KW_FLOAT:
                kind = Primitive.FLOAT;
                break;

            case KW_INT:
                kind = Primitive.INT;
                break;

            case KW_LONG:
                kind = Primitive.LONG;
                break;

            case KW_SHORT:
                kind = Primitive.SHORT;
                break;

            default:
                throw this.error(
                        token,
                        "unexpected %s; missing primitive type",
                        token
                );
        }

        return new PrimitiveType(token.getSourcePosition(), kind);
    }

    private ClassType parseClassType()
        throws ParserException, IOException
    {
        SourcePosition position = this.getSourcePosition();
        Name name = this.parseQualifiedIdentifier();
        List<TypeArgument> arguments;

        if (this.peek(Token.Kind.LT))
        {
            this.read();
            arguments = this.parseTypeArgumentList();
            this.expect(Token.Kind.GT);
        } else {
            arguments = Collections.emptyList();
        }

        return new ClassType(position, name, arguments);
    }

    private List<ClassType> parseClassTypeList()
        throws ParserException, IOException
    {
        List<ClassType> list = new ArrayList<ClassType>(3);

        list.add(this.parseClassType());
        while (this.peekRead(Token.Kind.COMMA))
        {
            list.add(this.parseClassType());
        }

        return list;
    }

    private ReferenceType parseReferenceType()
        throws ParserException, IOException
    {
        ReferenceType type;

        if (this.peek(Token.Kind.KW_BOOLEAN,
                      Token.Kind.KW_BYTE,
                      Token.Kind.KW_CHAR,
                      Token.Kind.KW_DOUBLE,
                      Token.Kind.KW_FLOAT,
                      Token.Kind.KW_INT,
                      Token.Kind.KW_LONG,
                      Token.Kind.KW_SHORT))
        {
            PrimitiveType primitive = this.parsePrimitiveType();

            if (!this.peek(Token.Kind.LBRACK)
                || !this.peekNextButOne(Token.Kind.RBRACK))
            {
                throw this.error(
                        primitive,
                        "unexpected %s; missing reference type",
                        primitive
                );
            }
            this.read();
            this.read();
            type = new ArrayType(primitive.getSourcePosition(), primitive);
        } else {
            type = this.parseClassType();
        }
        while (this.peek(Token.Kind.LBRACK)
                && this.peekNextButOne(Token.Kind.RBRACK))
        {
            this.read();
            this.read();
            type = new ArrayType(type.getSourcePosition(), type);
        }

        return type;
    }

    private List<ReferenceType> parseReferenceTypeList()
        throws ParserException, IOException
    {
        List<ReferenceType> list = new ArrayList<ReferenceType>(3);

        list.add(this.parseReferenceType());
        while (this.peekRead(Token.Kind.COMMA))
        {
            list.add(this.parseReferenceType());
        }

        return list;
    }

    /**
     * <pre>
     *   TypeParameter:
     *     Identifier [TypeBound]
     *
     *   TypeBound:
     *     extends TypeVariable
     *     extends ClassOrInterfaceType {AdditionalBound}
     *
     *   AdditionalBound:
     *     &amp; InterfaceType
     * </pre>
     */
    private TypeParameterDeclaration parseTypeParameter()
        throws ParserException, IOException
    {
        SourcePosition position = this.getSourcePosition();
        String name = this.readIdentifier();
        List<ReferenceType> bounds;

        if (this.peekRead(Token.Kind.KW_EXTENDS))
        {
            bounds = new ArrayList<ReferenceType>(3);
            bounds.add(this.parseReferenceType());
            while (this.peekRead(Token.Kind.BIT_AND))
            {
                bounds.add(this.parseReferenceType());
            }
        } else {
            bounds = Collections.emptyList();
        }

        return new TypeParameterDeclaration(position, name, bounds);
    }

    private List<TypeParameterDeclaration> parseTypeParameterList()
        throws ParserException, IOException
    {
        List<TypeParameterDeclaration> list = new ArrayList<TypeParameterDeclaration>(3);

        this.expect(Token.Kind.LT);
        list.add(this.parseTypeParameter());
        while (this.peekRead(Token.Kind.COMMA))
        {
            list.add(this.parseTypeParameter());
        }
        this.expect(Token.Kind.GT);

        return list;
    }

    /**
     * <pre>
     *   TypeArgument:
     *     ReferenceType
     *     Wildcard
     *
     *   Wildcard:
     *     ? [WildcardBounds]
     *
     *   WildcardBounds:
     *     extends ReferenceType
     *     super ReferenceType
     * </pre>
     */
    private TypeArgument parseTypeArgument()
        throws ParserException, IOException
    {
        if (this.peekRead(Token.Kind.CONDITIONAL))
        {
            SourcePosition position = this.getSourcePosition();
            Wildcard.Kind kind;
            ReferenceType bound;

            if (this.peekRead(Token.Kind.KW_EXTENDS))
            {
                kind = Wildcard.Kind.EXTENDS;
                bound = this.parseReferenceType();
            }
            else if (this.peekRead(Token.Kind.KW_SUPER))
            {
                kind = Wildcard.Kind.SUPER;
                bound = this.parseReferenceType();
            } else {
                kind = Wildcard.Kind.UNBOUND;
                bound = null;
            }

            return new Wildcard(position, kind, bound);
        }

        return this.parseReferenceType();
    }

    private List<TypeArgument> parseTypeArgumentList()
        throws ParserException, IOException
    {
        List<TypeArgument> list = new ArrayList<TypeArgument>(3);

        list.add(this.parseTypeArgument());
        while (this.peekRead(Token.Kind.COMMA))
        {
            list.add(this.parseTypeArgument());
        }

        return list;
    }

    private List<Expression> parseArguments()
        throws ParserException, IOException
    {
        List<Expression> list = null;

        this.expect(Token.Kind.LPAREN);
        while (!this.peek(Token.Kind.RPAREN))
        {
            Expression argument = this.toExpression(this.parseExpression());

            if (list == null)
            {
                list = new ArrayList<Expression>(3);
            }
            list.add(argument);
            if (!this.peekRead(Token.Kind.COMMA))
            {
                break;
            }
        }
        this.expect(Token.Kind.RPAREN);
        if (list == null)
        {
            return Collections.emptyList();
        } else {
            return list;
        }
    }

    private Expression toExpression(Atom atom)
        throws ParserException
    {
        if (atom instanceof Expression)
        {
            return (Expression) atom;
        }

        throw this.error(atom, "unexpected %s; missing expression", atom);
    }

    private AssignableExpression toAssignableExpression(Atom atom)
    {
        if (atom instanceof AssignableExpression)
        {
            return (AssignableExpression) atom;
        }

        throw this.error(atom, "unexpected %s; missing variable", atom);
    }

    private Token peek()
        throws ParserException, IOException
    {
        if (this.nextToken == null)
        {
            if (this.nextButOneToken == null)
            {
                this.nextToken = this.scanner.scan();
            } else {
                this.nextToken = this.nextButOneToken;
                this.nextButOneToken = null;
            }
        }

        return this.nextToken;
    }

    private boolean peek(Token.Kind expected)
        throws ParserException, IOException
    {
        return this.peek().getKind() == expected;
    }

    private boolean peek(Token.Kind first, Token.Kind... rest)
        throws ParserException, IOException
    {
        Token.Kind kind = this.peek().getKind();

        if (kind == first)
        {
            return true;
        }
        for (Token.Kind k : rest)
        {
            if (kind == k)
            {
                return true;
            }
        }

        return false;
    }

    private boolean peek(String identifier)
        throws ParserException, IOException
    {
        Token token = this.peek();

        return token.getKind() == Token.Kind.IDENTIFIER
            && identifier.equals(token.getText());
    }

    private boolean peekRead(Token.Kind expected)
        throws ParserException, IOException
    {
        if (this.peek(expected))
        {
            this.nextToken = null;

            return true;
        }

        return false;
    }

    private boolean peekRead(String identifier)
        throws ParserException, IOException
    {
        if (this.peek(identifier))
        {
            this.nextToken = null;

            return true;
        }

        return false;
    }

    private Token peekNextButOne()
        throws ParserException, IOException
    {
        if (this.nextButOneToken == null)
        {
            if (this.nextToken == null)
            {
                this.nextToken = this.scanner.scan();
            }
            this.nextButOneToken = this.scanner.scan();
        }

        return this.nextButOneToken;
    }

    private boolean peekNextButOne(Token.Kind expected)
        throws ParserException, IOException
    {
        return this.peekNextButOne().getKind() == expected;
    }

    private boolean peekNextButOne(String identifier)
        throws ParserException, IOException
    {
        Token token = this.peekNextButOne();

        return token.getKind() == Token.Kind.IDENTIFIER
            && identifier.equals(token.getText());
    }

    private Token read()
        throws ParserException, IOException
    {
        Token token;

        if (this.nextToken == null)
        {
            if (this.nextButOneToken == null)
            {
                token = this.scanner.scan();
            } else {
                token = this.nextButOneToken;
                this.nextButOneToken = null;
            }
        } else {
            token = this.nextToken;
            this.nextToken = null;
        }

        return token;
    }

    private String readIdentifier()
        throws ParserException, IOException
    {
        Token token = this.read();

        if (token.getKind() == Token.Kind.IDENTIFIER)
        {
            return token.getText();
        }

        throw this.error(token, "unexpected %s; missing identifier", token);
    }

    private void expect(Token.Kind expected)
        throws ParserException, IOException
    {
        Token token = this.read();

        if (token.getKind() != expected)
        {
            throw this.error(token, "unexpected %s; missing %s", token, expected);
        }
    }

    private void expect(String identifier)
        throws ParserException, IOException
    {
        Token token = this.read();

        if (token.getKind() != Token.Kind.IDENTIFIER
            || !identifier.equals(token.getText()))
        {
            throw this.error(token, "unexpected %s; missing `%s'", token, identifier);
        }
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
}
