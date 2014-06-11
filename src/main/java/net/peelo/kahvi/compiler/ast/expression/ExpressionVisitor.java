package net.peelo.kahvi.compiler.ast.expression;

public interface ExpressionVisitor<R, P>
    extends AssignableExpressionVisitor<R, P>
{
    R visitAssignmentExpression(AssignmentExpression e, P p);
    R visitCompoundAssignmentExpression(CompoundAssignmentExpression e, P p);
    R visitConditionalExpression(ConditionalExpression e, P p);
}
