package ar.edu.itba.lang.compiler;

import ar.edu.itba.lang.ast.*;

public interface NodeVisitor {

    public void visitAddNode(AddNode node);
    public void visitAndNode(AndNode node);
    public void visitArgsNode(ArgsNode argsNode);
    public void visitBlockNode(BlockNode node);
    public void visitCallNode(CallNode node);
    public void visitDivideNode(DivideNode node);
    public void visitFalseNode(FalseNode node);
    public void visitIfNode(IfNode node);
    public void visitIfElseNode(IfElseNode ifElseNode);
    public void visitLiteralNode(LiteralNode node);
    public void visitMultiplyNode(MultiplyNode node);
    public void visitNegateNode(NegateNode node);
    public void visitOrNode(OrNode node);
    public void visitSubstractNode(SubstractNode node);
    public void visitTrueNode(TrueNode node);
    public void visitWhileNode(WhileNode node);
}
