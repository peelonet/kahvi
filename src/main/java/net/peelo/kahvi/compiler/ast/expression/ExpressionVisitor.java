package net.peelo.kahvi.compiler.ast.expression;

public interface ExpressionVisitor<R, P>
    extends AssignableExpressionVisitor<R, P>
{
    R visitConditionalExpression(ConditionalExpression e, P p);
}
