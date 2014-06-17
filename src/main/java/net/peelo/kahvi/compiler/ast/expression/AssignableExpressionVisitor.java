package net.peelo.kahvi.compiler.ast.expression;

public interface AssignableExpressionVisitor<R, P>
{
    R visitIdentifierExpression(IdentifierExpression e, P p);
    R visitMemberSelectExpression(MemberSelectExpression e, P p);
    R visitPropertyExpression(PropertyExpression e, P p);
}
