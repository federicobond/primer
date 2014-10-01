package ar.edu.itba.lang.compiler;

import ar.edu.itba.lang.ast.*;

public interface NodeVisitor<T> {

    public T visitAddNode(AddNode node);
    public T visitAndNode(AndNode node);
    public T visitArgsNode(ArgsNode node);
    public T visitBlockNode(BlockNode node);
    public T visitCallNode(CallNode node);
    public T visitDivideNode(DivideNode node);
    public T visitFalseNode(FalseNode node);
    public T visitIfNode(IfNode node);
    public T visitIfElseNode(IfElseNode node);
    public T visitLiteralNode(LiteralNode node);
    public T visitMultiplyNode(MultiplyNode node);
    public T visitNegateNode(NegateNode node);
    public T visitOrNode(OrNode node);
    public T visitSubstractNode(SubstractNode node);
    public T visitTrueNode(TrueNode node);
    public T visitWhileNode(WhileNode node);
}
