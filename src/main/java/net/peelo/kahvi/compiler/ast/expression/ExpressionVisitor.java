package net.peelo.kahvi.compiler.ast.expression;

public interface ExpressionVisitor<R, P>
    extends AssignableExpressionVisitor<R, P>
{
    R visitAssignmentExpression(AssignmentExpression e, P p);
    R visitBinaryExpression(BinaryExpression e, P p);
    R visitBooleanLiteralExpression(BooleanLiteralExpression e, P p);
    R visitCompoundAssignmentExpression(CompoundAssignmentExpression e, P p);
    R visitConditionalExpression(ConditionalExpression e, P p);
    R visitInstanceOfExpression(InstanceOfExpression e, P p);
    R visitNullLiteralExpression(NullLiteralExpression e, P p);
}
