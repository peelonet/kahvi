package net.peelo.kahvi.compiler.ast.statement;

import net.peelo.kahvi.compiler.ast.Node;
import net.peelo.kahvi.compiler.ast.Scope;
import net.peelo.kahvi.compiler.ast.declaration.ExecutableDeclaration;
import net.peelo.kahvi.compiler.ast.declaration.ParameterDeclaration;
import net.peelo.kahvi.compiler.ast.declaration.TypeDeclaration;
import net.peelo.kahvi.compiler.util.SourcePosition;

import java.util.List;

public final class TryStatement extends Statement
{
    private final List<VariableStatement> resources;
    private final BlockStatement block;
    private final List<Catch> catches;
    private final BlockStatement finallyBlock;

    public TryStatement(SourcePosition position,
                        List<VariableStatement> resources,
                        BlockStatement block,
                        List<Catch> catches,
                        BlockStatement finallyBlock)
    {
        super(position);
        for (VariableStatement s : (this.resources = resources))
        {
            s.setEnclosingScope(this);
        }
        (this.block = block).setEnclosingScope(this);
        for (Catch c : (this.catches = catches))
        {
            c.setEnclosingTryStatement(this);
        }
        if ((this.finallyBlock = finallyBlock) != null)
        {
            this.finallyBlock.setEnclosingScope(this);
        }
    }

    public List<VariableStatement> getResources()
    {
        return this.resources;
    }

    public BlockStatement getBlock()
    {
        return this.block;
    }

    public List<Catch> getCatches()
    {
        return this.catches;
    }

    public BlockStatement getFinallyBlock()
    {
        return this.finallyBlock;
    }

    @Override
    public <R, P> R accept(StatementVisitor<R, P> visitor, P p)
    {
        return visitor.visitTryStatement(this, p);
    }

    @Override
    public String toString()
    {
        return "try {...}";
    }

    public static final class Catch extends Node implements Scope
    {
        private final ParameterDeclaration parameter;
        private final BlockStatement block;
        private TryStatement enclosingTryStatement;

        public Catch(SourcePosition position,
                     ParameterDeclaration parameter,
                     BlockStatement block)
        {
            super(position);
            (this.parameter = parameter).setEnclosingScope(this);
            (this.block = block).setEnclosingScope(this);
        }

        public ParameterDeclaration getParameter()
        {
            return this.parameter;
        }

        public BlockStatement getBlock()
        {
            return this.block;
        }

        public TryStatement getEnclosingTryStatement()
        {
            return this.enclosingTryStatement;
        }

        public synchronized void setEnclosingTryStatement(TryStatement enclosingTryStatement)
        {
            if (this.enclosingTryStatement != null
                && this.enclosingTryStatement != enclosingTryStatement)
            {
                throw new IllegalStateException("Enclosing try statement already set");
            }
            this.enclosingTryStatement = enclosingTryStatement;
        }

        @Override
        public Scope getEnclosingScope()
        {
            return this.enclosingTryStatement;
        }

        @Override
        public TypeDeclaration getEnclosingType()
        {
            if (this.enclosingTryStatement == null)
            {
                return null;
            } else {
                return this.enclosingTryStatement.getEnclosingType();
            }
        }

        @Override
        public ExecutableDeclaration getEnclosingExecutable()
        {
            if (this.enclosingTryStatement == null)
            {
                return null;
            } else {
                return this.enclosingTryStatement.getEnclosingExecutable();
            }
        }
    }
}
