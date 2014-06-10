package net.peelo.kahvi.compiler.ast.statement;

public interface StatementVisitor<R, P>
{
    R visitAssertStatement(AssertStatement s, P p);
    R visitBlockStatement(BlockStatement s, P p);
    R visitBreakStatement(BreakStatement s, P p);
    R visitCaseStatement(CaseStatement s, P p);
    R visitContinueStatement(ContinueStatement s, P p);
    R visitDoWhileStatement(DoWhileStatement s, P p);
    R visitEmptyStatement(EmptyStatement s, P p);
    R visitExpressionStatement(ExpressionStatement s, P p);
    R visitIfStatement(IfStatement s, P p);
    R visitLabeledStatement(LabeledStatement s, P p);
    R visitReturnStatement(ReturnStatement s, P p);
    R visitSwitchStatement(SwitchStatement s, P p);
    R visitSynchronizedStatement(SynchronizedStatement s, P p);
    R visitThrowStatement(ThrowStatement s, P p);
    R visitWhileStatement(WhileStatement s, P p);
}
