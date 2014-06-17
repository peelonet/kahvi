package net.peelo.kahvi.compiler.ast.statement;

import net.peelo.kahvi.compiler.ast.Node;
import net.peelo.kahvi.compiler.ast.Scope;
import net.peelo.kahvi.compiler.ast.declaration.ExecutableDeclaration;
import net.peelo.kahvi.compiler.ast.declaration.TypeDeclaration;
import net.peelo.kahvi.compiler.ast.expression.Expression;
import net.peelo.kahvi.compiler.util.SourcePosition;

import java.util.List;

/**
 * Representation of a 'switch' statement.
 *
 * <p> For example:
 * <pre>
 *   switch ( <em>expression</em> ) {
 *     <em>cases</em>
 *   }
 * </pre>
 */
public final class SwitchStatement extends BreakableStatement
{
    private final Expression expression;
    private final List<Case> cases;

    public SwitchStatement(SourcePosition position,
                           Expression expression,
                           List<Case> cases)
    {
        super(position);
        (this.expression = expression).setEnclosingScope(this);
        for (Case c : (this.cases = cases))
        {
            c.setEnclosingSwitchStatement(this);
        }
    }

    public Expression getExpression()
    {
        return this.expression;
    }

    public List<Case> getCases()
    {
        return this.cases;
    }

    @Override
    public <R, P> R accept(StatementVisitor<R, P> visitor, P p)
    {
        return visitor.visitSwitchStatement(this, p);
    }

    @Override
    public String toString()
    {
        return "switch (" + this.expression + ") {...}";
    }

    /**
     * Representation of a 'case' in a 'switch' statement.
     *
     * <p> For example:
     * <pre>
     *   case <em>expression</em> :
     *       <em>statements</em>
     *
     *   default :
     *       <em>statements</em>
     * </pre>
     */
    public static final class Case extends Node implements Scope
    {
        private final Expression expression;
        private final List<Statement> statements;
        private SwitchStatement enclosingSwitchStatement;

        public Case(SourcePosition position, List<Statement> statements)
        {
            this(position, null, statements);
        }

        public Case(SourcePosition position,
                    Expression expression,
                    List<Statement> statements)
        {
            super(position);
            if ((this.expression = expression) != null)
            {
                this.expression.setEnclosingScope(this);
            }
            for (Statement s : (this.statements = statements))
            {
                s.setEnclosingScope(this);
            }
        }

        public Expression getExpression()
        {
            return this.expression;
        }

        public List<Statement> getStatements()
        {
            return this.statements;
        }

        public SwitchStatement getEnclosingSwitchStatement()
        {
            return this.enclosingSwitchStatement;
        }

        public synchronized void setEnclosingSwitchStatement(SwitchStatement enclosingSwitchStatement)
        {
            if (this.enclosingSwitchStatement != null
                && this.enclosingSwitchStatement != enclosingSwitchStatement)
            {
                throw new IllegalStateException("Enclosing switch statement already set");
            }
            this.enclosingSwitchStatement = enclosingSwitchStatement;
        }

        @Override
        public Scope getEnclosingScope()
        {
            return this.enclosingSwitchStatement;
        }

        @Override
        public TypeDeclaration getEnclosingType()
        {
            if (this.enclosingSwitchStatement == null)
            {
                return null;
            } else {
                return this.enclosingSwitchStatement.getEnclosingType();
            }
        }

        @Override
        public ExecutableDeclaration getEnclosingExecutable()
        {
            if (this.enclosingSwitchStatement == null)
            {
                return null;
            } else {
                return this.enclosingSwitchStatement.getEnclosingExecutable();
            }
        }

        @Override
        public String toString()
        {
            if (this.expression == null)
            {
                return "default:";
            } else {
                return "case " + this.expression + ":";
            }
        }
    }
}
