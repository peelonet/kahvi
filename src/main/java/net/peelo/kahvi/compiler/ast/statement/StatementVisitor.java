package net.peelo.kahvi.compiler.ast.statement;

public interface StatementVisitor<R, P>
{
    R visitBlockStatement(BlockStatement s, P p);
    R visitBreakStatement(BreakStatement s, P p);
    R visitContinueStatement(ContinueStatement s, P p);
    R visitEmptyStatement(EmptyStatement s, P p);
}
