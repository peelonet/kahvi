package net.peelo.kahvi.compiler.ast;

import net.peelo.kahvi.compiler.ast.expression.ExpressionVisitor;
import net.peelo.kahvi.compiler.ast.type.TypeVisitor;

public interface AtomVisitor<R, P>
    extends ExpressionVisitor<R, P>, TypeVisitor<R, P> {}
