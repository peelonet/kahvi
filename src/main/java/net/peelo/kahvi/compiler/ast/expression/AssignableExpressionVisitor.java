package net.peelo.kahvi.compiler.ast.expression;

public interface AssignableExpressionVisitor<R, P>
{
    R visitArrayAccessExpression(ArrayAccessExpression e, P p);
    R visitIdentifierExpression(IdentifierExpression e, P p);
    R visitMemberSelectExpression(MemberSelectExpression e, P p);
    R visitParenthesizedExpression(ParenthesizedExpression e, P p);
    R visitPropertyExpression(PropertyExpression e, P p);
}
