package ar.edu.itba.lang.compiler;

import ar.edu.itba.lang.ast.*;

public interface NodeVisitor {

    public void visitAddNode(AddNode node);
    public void visitAndNode(AndNode node);
    public void visitBlockNode(BlockNode node);
    public void visitDivideNode(DivideNode node);
    public void visitFalseNode(FalseNode node);
    public void visitIfNode(IfNode node);
    public void visitLiteralNode(LiteralNode node);
    public void visitNegateNode(NegateNode node);
    public void visitOrNode(OrNode node);
    public void visitSubstractNode(SubstractNode node);
    public void visitTrueNode(TrueNode node);
    public void visitWhileNode(WhileNode node);

}
