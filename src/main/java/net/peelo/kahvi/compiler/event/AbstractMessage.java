package net.peelo.kahvi.compiler.event;

import net.peelo.kahvi.compiler.ast.Node;
import net.peelo.kahvi.compiler.ast.Scope;
import net.peelo.kahvi.compiler.ast.declaration.ExecutableDeclaration;
import net.peelo.kahvi.compiler.ast.declaration.FieldDeclaration;
import net.peelo.kahvi.compiler.ast.declaration.TypeDeclaration;
import net.peelo.kahvi.compiler.ast.expression.Expression;
import net.peelo.kahvi.compiler.ast.statement.ExpressionStatement;
import net.peelo.kahvi.compiler.ast.statement.Statement;
import net.peelo.kahvi.compiler.util.SourceLocatable;
import net.peelo.kahvi.compiler.util.SourcePosition;

import java.io.File;
import java.io.PrintStream;

public abstract class AbstractMessage implements SourceLocatable
{
    /** Location in the source code where the message occurred. */
    private final SourcePosition position;
    /** AST node which this message is related to. */
    private final Node source;
    /** The actual message. */
    private final String message;

    AbstractMessage(SourcePosition position,
                    Node source,
                    String message)
    {
        this.position = position;
        this.source = source;
        this.message = message;
    }

    /**
     * Returns the position in the source code where this message occurred, or
     * {@code null} if no such information is available.
     */
    @Override
    public final SourcePosition getSourcePosition()
    {
        return this.position;
    }

    /**
     * Returns the AST node which caused this message to occur, or {@code null}
     * if no such information is available.
     */
    public final Node getSource()
    {
        return this.source;
    }

    /**
     * Returns the actual message.
     */
    public final String getMessage()
    {
        return this.message;
    }

    public final void print(PrintStream output)
    {
        StringBuilder sb = new StringBuilder();

        if (this.position != null)
        {
            File file = this.position.getFile();
            int line = this.position.getLineNumber();
            int column = this.position.getColumnNumber();

            if (file != null)
            {
                sb.append(file).append(':');
            }
            if (line > 0)
            {
                sb.append(line).append(':');
            }
            if (column > 0)
            {
                sb.append(column).append(':');
            }
        }
        sb.append(this.toString());
        output.println(sb.toString());
        if (this.source instanceof Expression
            || this.source instanceof Statement
            || this.source instanceof TypeDeclaration
            || this.source instanceof ExecutableDeclaration
            || this.source instanceof FieldDeclaration)
        {
            Node node = this.source;

            if (node instanceof Expression)
            {
                Scope scope = ((Expression) node).getEnclosingScope();

                if (scope instanceof ExpressionStatement)
                {
                    node = (Statement) scope;
                }
            }
            sb.setLength(0);
            sb.append("      ").append(node);
            output.println(sb.toString());
            output.println();
        }
    }

    /**
     * Returns textual information about the message.
     */
    @Override
    public String toString()
    {
        return this.message;
    }
}
